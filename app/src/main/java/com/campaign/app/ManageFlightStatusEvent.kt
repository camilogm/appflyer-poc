package com.campaign.app

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.appsflyer.deeplink.DeepLink
import com.appsflyer.deeplink.DeepLinkResult
import org.json.JSONObject
import org.json.JSONException


val LOG_TAG = "FLIGHT_STATUS_TRACK";


fun handleDeepLink(context: Context, deepLinkResult: DeepLinkResult) {
    var deepLinkObj: DeepLink = deepLinkResult.deepLink
    try {
        Log.d(LOG_TAG, "The DeepLink data is: $deepLinkObj")

        val deepLinkJson = JSONObject(deepLinkObj.toString())

        val date = deepLinkJson.optString("date")
        val originStation = deepLinkJson.optString("originStation")
        val destinationStation = deepLinkJson.optString("destinationStation")
        val deepLinkValue = deepLinkJson.optString("deep_link_value")
        val mediaSource = deepLinkJson.optString("media_source")
        val campaign = deepLinkJson.optString("campaign")

        val extractedData = """
            Date: $date
            Origin Station: $originStation
            Destination Station: $destinationStation
            Deep Link Value: $deepLinkValue
            Media Source: $mediaSource
            Campaign: $campaign
        """.trimIndent()

        Log.d(LOG_TAG, "about to open result")
        showPopupDialog(context, "DeepLink Information", extractedData)

    } catch (e: JSONException) {
        Log.e(LOG_TAG, "Error parsing deep link JSON", e)
    }
}

fun showPopupDialog(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}