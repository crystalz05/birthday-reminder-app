// supabase/functions/send-birthday-reminders/index.ts
import { serve } from "https://deno.land/std@0.177.0/http/server.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";
import { GoogleAuth } from "npm:google-auth-library@9.0.0";
// --- Supabase setup ---
const URL = Deno.env.get("URL");
const SERVICE_ROLE_KEY = Deno.env.get("SERVICE_ROLE_KEY");
if (!URL || !SERVICE_ROLE_KEY)
  throw new Error("Missing Supabase URL or SERVICE_ROLE_KEY");
const supabase = createClient(URL, SERVICE_ROLE_KEY);
// --- Firebase service account ---
// const SERVICE_ACCOUNT_JSON = Deno.env.get("FIREBASE_SERVICE_ACCOUNT_JSON");
// if (!SERVICE_ACCOUNT_JSON) throw new Error("Missing FIREBASE_SERVICE_ACCOUNT_JSON env");
// const serviceAccount = JSON.parse(SERVICE_ACCOUNT_JSON);
const SERVICE_ACCOUNT_B64 = Deno.env.get("FIREBASE_SERVICE_ACCOUNT_JSON_B64");
if (!SERVICE_ACCOUNT_B64)
  throw new Error("Missing FIREBASE_SERVICE_ACCOUNT_B64 env");
// decode Base64 to get the JSON object
const serviceAccount = JSON.parse(
  new TextDecoder().decode(
    Uint8Array.from(atob(SERVICE_ACCOUNT_B64), (c) => c.charCodeAt(0))
  )
);
// FCM v1 endpoint
const FCM_SEND_URL = `https://fcm.googleapis.com/v1/projects/${serviceAccount.project_id}/messages:send`;
// Timezone (IANA format, e.g., "Africa/Lagos")
const TIMEZONE = Deno.env.get("TIMEZONE") ?? "UTC";
// GoogleAuth for FCM
const auth = new GoogleAuth({
  credentials: serviceAccount,
  scopes: ["https://www.googleapis.com/auth/firebase.messaging"],
});
// --- Get short-lived access token ---
async function getAccessToken() {
  const client = await auth.getClient();
  const res = await client.getAccessToken();
  if (!res?.token) throw new Error("Failed to obtain access token");
  return res.token;
}
// --- Helper: returns "MM-DD" for today in the configured timezone ---
function getTodayMonthDay(timezone) {
  const now = new Date();
  const parts = now.toLocaleString("en-CA", {
    timeZone: timezone,
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
  return parts.slice(5, 10); // "MM-DD"
}
// --- Edge function ---
serve(async (_req) => {
  try {
    const todayMMDD = getTodayMonthDay(TIMEZONE);
    // Fetch all contacts
    const { data: contacts, error: contactsError } = await supabase
      .from("contacts")
      .select("id, fullName, birthday, userId");
    if (contactsError) throw contactsError;
    // Filter contacts whose birthday is today
    const todaysBirthdays = (contacts ?? []).filter((c) => {
      if (!c.birthday) return false;
      return String(c.birthday).slice(5, 10) === todayMMDD;
    });
    if (!todaysBirthdays.length) {
      return new Response(
        JSON.stringify({
          message: "No birthdays today",
        }),
        {
          status: 200,
        }
      );
    }
    // Get a single FCM access token
    const accessToken = await getAccessToken();
    let sentCount = 0;
    for (const contact of todaysBirthdays) {
      // Fetch tokens for the user
      const { data: tokens, error: tokenError } = await supabase
        .from("user_tokens")
        .select("fcm_token")
        .eq("user_id", contact.userId);
      if (tokenError) {
        console.error("Token fetch error for user", contact.userId, tokenError);
        continue;
      }
      if (!tokens || !tokens.length) continue;
      for (const t of tokens) {
        const messageBody = {
          message: {
            token: t.fcm_token,
            notification: {
              title: "🎉 Birthday Reminder",
              body: `Today is ${contact.fullName}'s birthday!`,
            },
            data: {
              contactId: contact.id ?? "",
              userId: contact.userId ?? "",
            },
            android: {
              notification: {
                click_action: "MAIN_ACTIVITY",
              },
            },
          },
        };
        // Send notification
        const res = await fetch(FCM_SEND_URL, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(messageBody),
        });
        if (!res.ok) {
          const text = await res.text();
          console.error("FCM send failed:", res.status, text);
          // Remove token if unregistered
          if (text.includes("UNREGISTERED")) {
            await supabase
              .from("user_tokens")
              .delete()
              .eq("fcm_token", t.fcm_token);
            console.log("Removed unregistered token:", t.fcm_token);
          }
          continue;
        }
        console.log("Sent notification to", t.fcm_token);
        sentCount++;
      }
    }
    return new Response(
      JSON.stringify({
        message: `Sent ${sentCount} notifications`,
      }),
      {
        status: 200,
      }
    );
  } catch (err) {
    console.error("Function error:", err);
    return new Response(
      JSON.stringify({
        error: err?.message ?? String(err),
      }),
      {
        status: 500,
      }
    );
  }
});
