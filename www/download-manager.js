module.exports = window.DownloadManager = DownloadManager = {

    startDownload: function (url, path, headers, progressback, callback) {

        success = function(list) {callback(null, list);}
        error = function(err) {callback(err);}

        args = [url, path, headers]

        return cordova.exec(success, error, "DownloadManagerPlugin", "download", args);
    }


    addCompletedDownload: function (title,
                description,
                isMediaScannerScannable,
                mimeType,
                path,
                length,
                showNotification,
                uri,
                referer){
                  success = function(id) {callback(null, id);}
                  error = function(err) {callback(err);}

                  args = [title,
                              description,
                              isMediaScannerScannable,
                              mimeType,
                              path,
                              length,
                              showNotification,
                              uri,
                              referer]
                }
                return cordova.exec(success, error, "DownloadManagerPlugin", "addCompletedDownload", args);
};
