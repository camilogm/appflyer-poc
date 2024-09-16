package com.campaign.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.campaign.app.ui.theme.MyApplicationTheme

import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult


class MainActivity : ComponentActivity() {
    val LOG_TAG = "AppFlyerTest";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the deep link URL
                    DeepLinkButton(
                        this,
                        "https://optimalbills.onelink.me/uj0L/3nup5oeu",
                        "MEX" ,
                        "ACA",
                        "2024-09-15"
                    )
                }
            }
        }


        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionDataMap: Map<String, Any>) {
            }

            override fun onConversionDataFail(errorMessage: String) {
            }

            override fun onAppOpenAttribution(attributionData: Map<String, String>) {
            }

            override fun onAttributionFailure(errorMessage: String) {
            }
        }


        val appsFlyerKey = BuildConfig.APPSFLYER_DEV_KEY;
        AppsFlyerLib.getInstance().init(appsFlyerKey, conversionListener, this)


        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().start(applicationContext, "", object : AppsFlyerRequestListener {
            override fun onSuccess() {
                Log.d("Test", "suscribed")
            }

            override fun onError(i: Int, s: String) {
                Log.d("Test", "failed sub")
            }
        })


        AppsFlyerLib.getInstance().subscribeForDeepLink(object : DeepLinkListener{
            override fun onDeepLinking(deepLinkResult: DeepLinkResult) {
                when (deepLinkResult.status) {
                    DeepLinkResult.Status.FOUND -> {
                        Log.d(
                            LOG_TAG,"Deep link found"
                        )
                        runOnUiThread {
                            handleDeepLink(this@MainActivity, deepLinkResult)
                        }

                    }
                    DeepLinkResult.Status.NOT_FOUND -> {
                        Log.d(
                            LOG_TAG,"Deep link not found"
                        )
                        return
                    }
                    else -> {
                        // dlStatus == DeepLinkResult.Status.ERROR
                        val dlError = deepLinkResult.error
                        Log.d(
                            LOG_TAG,"There was an error getting Deep Link data: $dlError"
                        )
                        return
                    }
                }

            }
        })
    }
}

@Composable
fun DeepLinkButton(context: Context, url: String, originStation: String, destination: String, date: String) {
    // Build the deep link URL dynamically with parameters
    val deepLinkUrl = Uri.parse(url)
        .buildUpon()
        .appendQueryParameter("originStation", originStation)
        .appendQueryParameter("destinationStation", destination)
        .appendQueryParameter("date", date)
        .build()
        .toString()

    // Get the clipboard manager from the context
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // Button to copy the deep link URL to the clipboard
        Button(onClick = {
            // Create a ClipData object and copy the deep link URL to the clipboard
            val clip = ClipData.newPlainText("Deep Link URL", deepLinkUrl)
            clipboardManager.setPrimaryClip(clip)
        }) {
            Text(text = "Copy Deep Link to Clipboard")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}