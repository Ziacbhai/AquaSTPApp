package Models;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ziac.aquastpapp.Activities.Global;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<String> {
    private  final  Response.Listener<String> mListener;
    private final Response.ErrorListener mErrorListener;
    private final  Map<String, String> mParams;
    private  final File mFile;
    private final InputStream minputStream;


    public VolleyMultipartRequest(String url, File file, InputStream inputStream, Map<String, String> params, Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mFile=file;
        this.mParams=params;
        this.minputStream = inputStream;

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams  ;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers=new HashMap<>();
        String accesstoken = Global.sharedPreferences.getString("access_token", null).toString();
        headers.put("Authorization", "Bearer " + accesstoken);
        //headers.put("test", "Bearer " + accesstoken);


        for (Map.Entry<String, String> entry : mParams.entrySet()) {
            //appendParameter(bos, entry.getKey(), entry.getValue());
            headers.put(entry.getKey(), entry.getValue());
        }

        headers.put("file_name", mFile.getName());


        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data" ;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError("Unsupported Encoding: " + e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            //appendParameter(bos, "file", mFile);
            appendParameter(bos, "file_name", mFile,minputStream);
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                appendParameter(bos, entry.getKey(), entry.getValue());
            }
            bos.write(("--" + "***" + "--" + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private void appendParameter(ByteArrayOutputStream bos, String key, String value) throws IOException {
        bos.write(("--" + "***" + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
        bos.write(value.getBytes());
        bos.write(("\r\n").getBytes());
    }

    private void appendParameter(ByteArrayOutputStream bos, String key, File file, InputStream fileInputStream) throws IOException {
        bos.write(("--" + "***" + "\r\n").getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        bos.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());

        //FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bos.write(("\r\n").getBytes());
        fileInputStream.close();
    }

}
