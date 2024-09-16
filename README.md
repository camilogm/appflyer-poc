# Configuration
Setup environment variable with the appflyer development key

APPSFLYER_DEV_KEY="yourkey"

You can get it by login into the appsflyer portal and looking for  the registered application with the app bundle or by creating a new subscription

Also, in seek of be sure about your configuration you must ensure to SHA-256 file the app is encrypted with it's the same that it's setup on appsflyer template


adb shell pm get-app-links --user 0 com.campaign.app


