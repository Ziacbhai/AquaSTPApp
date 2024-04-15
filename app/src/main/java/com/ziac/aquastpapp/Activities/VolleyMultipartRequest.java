package com.ziac.aquastpapp.Activities;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
  
    private final Map<String, String> mHeaders;
    private final Response.ErrorListener mErrorListener;
    private final Response.Listener<NetworkResponse> mListener;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = new HashMap<>();
    }
    
    public VolleyMultipartRequest(int method, String url, Response.Listener<NetworkResponse> listener, @Nullable Response.ErrorListener errorListener, Map<String, String> mHeaders, Response.ErrorListener mErrorListener, Response.Listener<NetworkResponse> mListener) {
        super(method, url, errorListener);
        this.mHeaders = mHeaders;
        this.mErrorListener = mErrorListener;
        this.mListener = mListener;
    }



    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(com.android.volley.VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }
    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Add parameters
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, "");
            }

            // Add files
            Map<String, DataPart> files = getByteData();
            if (files != null && files.size() > 0) {
                fileParse(dos, files);
            }

            // Close multipart form data after text and file data
            dos.writeBytes("--" + boundary + "--");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String key, String value) throws IOException {
        dataOutputStream.writeBytes("--" + boundary + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + value + "\r\n");
    }

    private void fileParse(DataOutputStream dataOutputStream, Map<String, DataPart> files) throws IOException {
        for (Map.Entry<String, DataPart> entry : files.entrySet()) {
            buildFilePart(dataOutputStream, entry.getKey(), entry.getValue());
        }
    }

    private void buildFilePart(DataOutputStream dataOutputStream, String key, DataPart file) throws IOException {
        dataOutputStream.writeBytes("--" + boundary + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                key + "\"; filename=\"" + file.getFileName() + "\"\r\n");
        if (file.getType() != null && !file.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + file.getType() + "\r\n");
        }
        dataOutputStream.writeBytes("\r\n");
        dataOutputStream.write(file.getContent());
        dataOutputStream.writeBytes("\r\n");
    }

    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return null;
    }
}
