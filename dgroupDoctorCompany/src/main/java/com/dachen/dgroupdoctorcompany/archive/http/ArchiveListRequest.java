package com.dachen.dgroupdoctorcompany.archive.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.dachen.common.toolbox.DCommonRequest;
import com.dachen.common.utils.VolleyUtil;
import com.dachen.dgroupdoctorcompany.app.Constants;
import com.dachen.imsdk.ImSdk;
import com.dachen.medicine.config.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mcp on 2016/1/12.
 */
public class ArchiveListRequest extends DCommonRequest {
    public int pageIndex;
    public String mode;
    public String nameKey;
    public String type;
    public int order = -1;//1=顺序（默认），-1=倒序

    public ArchiveListRequest(int pageIndex, String nameKey, String type, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, AppConfig.getUrl(Constants.GET_UPLOADED_FILE_LIST,1), listener, errorListener);
        this.pageIndex = pageIndex;
        this.mode = mode;
        this.nameKey = nameKey;
        this.type = type;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> m = new HashMap<>();
        m.put("access_token", ImSdk.getInstance().accessToken);
        m.put("pageIndex", pageIndex + "");
        m.put("type", type);
        m.put("fileNameKey", nameKey);
        m.put("sortAttr", "date");
        m.put("sortType", "-1");
        m.put("pageSize", "20");
        return VolleyUtil.checkParam(m);
    }
}
