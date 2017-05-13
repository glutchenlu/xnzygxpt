package develop.cl.com.crsp.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class MyStringRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private Map<String, String> mMap;

    public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> map) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mMap = map;
    }

    public MyStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> map) {
        this(0, url, listener, errorListener, map);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.mMap;
    }

    protected void deliverResponse(String response) {
        this.mListener.onResponse(response);
    }

    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException var4) {
            parsed = new String(response.data);
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
