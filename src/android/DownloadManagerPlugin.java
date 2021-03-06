package io.cozy.imagesbrowser;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;

public class DownloadManagerPlugin extends CordovaPlugin {

    /**
     * Constructor.
     */
    public DownloadManagerPlugin() {
    }

    private static final class RequestContext {
        String source;
        String target;
        File targetFile;
        Long referece;
        CallbackContext callbackContext;
        boolean aborted;
        RequestContext(String source, String target, CallbackContext callbackContext) {
            this.source = source;
            this.target = target;
            this.callbackContext = callbackContext;
        }
        void sendPluginResult(PluginResult pluginResult) {
            synchronized (this) {
                if (!aborted) {
                    callbackContext.sendPluginResult(pluginResult);
                }
            }
        }
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArray of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("startDownload")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }

                    Url source = args.getString(0)
                    JSONObject headers = args.optJSONObject(2);
                    RequestContext rcontext = new RequestContext(source)

                    startDownload(rcontext);
                }
            });
        }
        else if(action.equals('addCompletedDownload')){
          JSONObject params = args.getJSONObject(0);
          String title = params.getString('title') ;
                String description = params.getString('description');
                boolean isMediaScannerScannable = params.getBoolean('isMediaScannerScannable');
                String mimeType = params.getString('mimeType') ;
                String path = params.getString('path');
                long length = params.getLong('length') ;
                boolean showNotification = params.getBoolean('showNotification');
                Uri uri = params.get('uri';)
                Uri referer = params.get('referer');

                addCompletedDownload(title,
                            description,
                            isMediaScannerScannable,
                            mimeType,
                            path,
                            length,
                            showNotification,
                            uri,
                            referer)
        }
        else {
            return false;
        }
        return true;
    }


    @SuppressLint("NewApi")
    public JSONArray startDownload(RequestContext rcontext){
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(rcontext.source);
        DownloadManager.Request request = new Request(uri);
        RequestContext.reference = downloadmanager.enqueue(request);

    }

    @SuppressLint("NewApi")
    public long addCompletedDownload (String title,
                String description,
                boolean isMediaScannerScannable,
                String mimeType,
                String path,
                long length,
                boolean showNotification,
                Uri uri,
                Uri referer){
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
Uri myUri = Uri.parse(uri);
Uri refererUri = Uri.parse(referer);


long result = downloadmanager.addCompletedDownload(title,
            description,
            isMediaScannerScannable,
            mimeType,
            path,
            length,
            showNotification,
            myUri,
            refererUri);
callbackContext.sendPluginResult(result);

    }



}
