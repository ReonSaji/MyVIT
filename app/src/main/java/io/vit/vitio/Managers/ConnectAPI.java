/*
 * Copyright (c) 2015 GDG VIT Vellore.
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.vit.vitio.Managers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.Parsers.ParseCoursePage;
import io.vit.vitio.Managers.Parsers.ParseGeneral;
import io.vit.vitio.Managers.Parsers.ParseResponse;
import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.Managers.Parsers.ParseSpotlight;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by shalini on 18-06-2015.
 */
public class ConnectAPI{


    //Constants
    public static final int SERVERTEST_CODE = 0;
    public static final int LOGIN_CODE = 1;
    public static final int REFRESH_CODE = 2;
    public static final int COURSE_PAGE_SLOTS_CODE = 3;
    public static final int COURSE_PAGE_FACULTIES_CODE = 4;
    public static final int COURSE_PAGE_UPLOADS_CODE = 5;
    public static final int PROFILE_IMAGE_CODE = 6;
    public static final int GENERIC_CODE = -1;
    private static final int DEFAULT_MAX_TRIES = 2;
    //Initialize HTTP URLs
    /*private final String VELLORE_LOGIN_URL = "http://vitacademics-rel.herokuapp.com/api/v2/vellore/login";
    private final String CHENNAI_LOGIN_URL = "http://vitacademics-rel.herokuapp.com/api/v2/chennai/login";
    private final String VELLORE_REFRESH_URL = "http://vitacademics-rel.herokuapp.com/api/v2/vellore/refresh";
    private final String CHENNAI_REFRESH_URL = "http://vitacademics-rel.herokuapp.com/api/v2/chennai/refresh";
    private final String SERVERTEST_URL = "http://vitacademics-rel.herokuapp.com/api/v2/system";
    private final String SPOTLIGHT_URL = "http://facademics-test.appspot.com/spotlight";
    private final String COURSE_PAGE_SLOTS_URL = "http://academics.azurewebsites.com/coursepage/slots";
    private final String COURSE_PAGE_FACULTIES_URL = "http://academics.azurewebsites.com/coursepage/faculties";
    private final String COURSE_PAGE_UPLOADS_URL = "http://academics.azurewebsites.com/coursepage/data";
    */
    private final String VELLORE_LOGIN_URL = "http://myffcs.in:8080/campus/vellore/login";
    private final String CHENNAI_LOGIN_URL = "http://myffcs.in:8080/campus/chennai/login";
    private final String VELLORE_REFRESH_URL = "http://myffcs.in:8080/campus/vellore/refresh";
    private final String CHENNAI_REFRESH_URL = "http://myffcs.in:8080/campus/chennai/refresh";
    private final String SERVERTEST_URL = "http://vitacademics-rel.herokuapp.com/api/v2/system";
    private final String SPOTLIGHT_URL = "http://facademics-test.appspot.com/spotlight";
    private final String COURSE_PAGE_SLOTS_URL = "http://myffcs.in:8080/campus/vellore/coursepage/slots/";
    private final String COURSE_PAGE_FACULTIES_URL = "http://myffcs.in:8080/campus/vellore/coursepage/faculties/";
    private final String COURSE_PAGE_UPLOADS_URL = "http://myffcs.in:8080/campus/vellore/coursepage/data/";
    private final String PROFILE_IMAGE_URL = "http://myffcs.in:8080/campus/vellore/pic";



    private DataHandler dataHandler;
    private Context mContext;
    private AppController appController;
    private RequestListener mListener;

    public ConnectAPI(Context context) {
        mContext = context;
        dataHandler = DataHandler.getInstance(mContext);
        appController = AppController.getInstance();

    }

    public void serverTest() {
        if (mListener != null) {
            mListener.onRequestInitiated(SERVERTEST_CODE);

            //Skipping testing
            ParseResponse parseResponse = new ParseResponse("");
            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
            mListener.onRequestCompleted(parcel, SERVERTEST_CODE);

            /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    SERVERTEST_URL.trim(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response", "recieved");
                    if (response != null) {
                        ParseResponse parseResponse = new ParseResponse(response);
                        ReturnParcel parcel = new ReturnParcel(parseResponse.getResponseStatusCode());
                        mListener.onRequestCompleted(parcel, SERVERTEST_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, SERVERTEST_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, SERVERTEST_CODE);
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    Log.d("pnresponse", response.toString());
                    return super.parseNetworkResponse(response);
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(jsonObjectRequest, "servertest");*/
        } else {
            return;
        }
    }

    public void login() {
        if (mListener != null) {


            mListener.onRequestInitiated(LOGIN_CODE);

            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("loginresponse", response);
                    if (response != null) {
                        ParseGeneral parseGeneral = new ParseGeneral(response);
                        ReturnParcel parcel = new ReturnParcel(parseGeneral.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseGeneral);
                        if (parseGeneral.validateLogin()) {
                            mListener.onRequestCompleted(parcel, LOGIN_CODE);
                        } else {
                            mListener.onErrorRequest(parcel, LOGIN_CODE);
                        }

                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, LOGIN_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, LOGIN_CODE);
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());

                    return postMap;
                }


                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "loginrequest");
        } else {
            return;
        }
    }

    public boolean loginSynchronous() {
        try {
            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }

            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), future, future) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());

                    return postMap;
                }

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest);
            String response = future.get();
            Log.i(TAG, "loginSynchronous: "+response);
            if (response != null) {
                ParseGeneral parseGeneral = new ParseGeneral(response);
                if (parseGeneral.validateLogin()) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }

    public void refresh() {
        if (mListener != null) {
            mListener.onRequestInitiated(REFRESH_CODE);
            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_REFRESH_URL;
            } else
                url = CHENNAI_REFRESH_URL;

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {

                        Log.d("refresh", "response");
                        ParseCourses parseCourses = new ParseCourses(response);
                        ReturnParcel parcel = new ReturnParcel(parseCourses.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseCourses);
                        if (parseCourses.getResponseStatusCode() == ErrorDefinitions.CODE_SUCCESS || parseCourses.getResponseStatusCode() == ErrorDefinitions.CODE_MONGODOWM) {

                            dataHandler.saveCourseList((ArrayList<Course>)parseCourses.getCoursesList());
                            dataHandler.saveSemester(parseCourses.getSemester());
                            dataHandler.saveSchool(parseCourses.getSchool());
                            dataHandler.saveName(parseCourses.getName());
                            dataHandler.saveFacultyAdvisor(parseCourses.getFacultyAdvisor());
                            dataHandler.saveAcademicHistory(parseCourses.getAcademicHistory());
                            if(parseCourses.getCoursesList()!=null&&parseCourses.getCoursesList().size()>0)
                            {
                                Intent intent=new Intent(mContext,SendDataToWearableService.class);
                                mContext.startService(intent);
                            }
                        }
                        mListener.onRequestCompleted(parcel, REFRESH_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, REFRESH_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, REFRESH_CODE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());


                    return postMap;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    Log.i(TAG, "parseNetworkResponse: "+response.toString());
                    return super.parseNetworkResponse(response);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    Log.i(TAG, "parseNetworkError: "+volleyError.getCause());;
                    return super.parseNetworkError(volleyError);
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(16000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "refreshrequest");
        } else {
            return;
        }
    }

    public void fetchSpotlight() {
        if (mListener != null) {
            mListener.onRequestInitiated(GENERIC_CODE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    SPOTLIGHT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("sres", response);
                    if (response != null) {
                        ParseSpotlight parseSpotlight = new ParseSpotlight(response);
                        parseSpotlight.parse();
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
                        parcel.setRETURN_PARCEL_OBJECT(parseSpotlight);
                        mListener.onRequestCompleted(parcel, GENERIC_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, GENERIC_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, GENERIC_CODE);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "spotlightrequest");
        } else {
            return;
        }
    }

    public void fetchProfileImage(){
        if (mListener != null) {
            mListener.onRequestInitiated(PROFILE_IMAGE_CODE);

            //TODO Define getPassword function in Data Handler after new api
            String url = PROFILE_IMAGE_URL;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("image", response);
                    if (response != null&&!response.equals("null")&&!response.equals("")) {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
                        parcel.setRETURN_PARCEL_OBJECT(response);
                        dataHandler.saveProfileImageEncoded(response);
                        mListener.onRequestCompleted(parcel, PROFILE_IMAGE_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, COURSE_PAGE_SLOTS_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, PROFILE_IMAGE_CODE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> paramsMap = new HashMap<>();
                    paramsMap.put("regNo", dataHandler.getRegNo());
                    paramsMap.put("psswd", dataHandler.getPassword());
                    return paramsMap;

                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setShouldCache(true);
            appController.addToRequestQueue(stringRequest, "profileImageRequest");

        } else {
            return;
        }
    }

    public void loginAndFetchProfileImage(){
        if (mListener != null) {


            mListener.onRequestInitiated(LOGIN_CODE);

            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("loginresponse", response);
                    if (response != null) {
                        ParseGeneral parseGeneral = new ParseGeneral(response);
                        ReturnParcel parcel = new ReturnParcel(parseGeneral.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseGeneral);
                        if (parseGeneral.validateLogin()) {
                            fetchProfileImage();
                        } else {
                            mListener.onErrorRequest(parcel, LOGIN_CODE);
                        }

                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, LOGIN_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, LOGIN_CODE);
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());

                    return postMap;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                    ConnectAPI.DEFAULT_MAX_TRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "loginrequest");
        } else {
            return;
        }
    }

    public void loginAndGetCoursePageSlots(final String courseId) {
        if (mListener != null) {


            mListener.onRequestInitiated(LOGIN_CODE);

            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("loginresponse", response);
                    if (response != null) {
                        ParseGeneral parseGeneral = new ParseGeneral(response);
                        ReturnParcel parcel = new ReturnParcel(parseGeneral.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseGeneral);
                        if (parseGeneral.validateLogin()) {
                            getCoursePageSlots(courseId);
                        } else {
                            mListener.onErrorRequest(parcel, LOGIN_CODE);
                        }

                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, LOGIN_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, LOGIN_CODE);
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());

                    return postMap;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                    ConnectAPI.DEFAULT_MAX_TRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "loginrequest");
        } else {
            return;
        }
    }

    public void loginAndGetCoursePageFaculties(final String courseId, final String slotId) {
        if (mListener != null) {


            mListener.onRequestInitiated(LOGIN_CODE);

            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("loginresponse", response);
                    if (response != null) {
                        ParseGeneral parseGeneral = new ParseGeneral(response);
                        ReturnParcel parcel = new ReturnParcel(parseGeneral.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseGeneral);
                        if (parseGeneral.validateLogin()) {
                            getCoursePageFaculties(courseId,slotId);
                        } else {
                            mListener.onErrorRequest(parcel, LOGIN_CODE);
                        }

                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, LOGIN_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, LOGIN_CODE);
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());

                    return postMap;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                    ConnectAPI.DEFAULT_MAX_TRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "loginrequest");
        } else {
            return;
        }
    }
    public void loginAndGetCoursePageUploads(final String courseId, final String slotId, final String facId) {
        if (mListener != null) {


            mListener.onRequestInitiated(LOGIN_CODE);

            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("loginresponse", response);
                    if (response != null) {
                        ParseGeneral parseGeneral = new ParseGeneral(response);
                        ReturnParcel parcel = new ReturnParcel(parseGeneral.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseGeneral);
                        if (parseGeneral.validateLogin()) {
                            getCoursePageUploads(courseId,slotId,facId);
                        } else {
                            mListener.onErrorRequest(parcel, LOGIN_CODE);
                        }

                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel, LOGIN_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel, LOGIN_CODE);
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String, String> postMap = new HashMap<>();
                    postMap.put("regNo", dataHandler.getRegNo());
                    //postMap.put("dob", dataHandler.getDOB());
                    //postMap.put("mobile", dataHandler.getPhoneNo());
                    postMap.put("psswd", dataHandler.getPassword());

                    return postMap;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                    ConnectAPI.DEFAULT_MAX_TRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "loginrequest");
        } else {
            return;
        }
    }

    private void getCoursePageSlots(final String courseId) {
        if (mListener != null) {
            mListener.onRequestInitiated(COURSE_PAGE_SLOTS_CODE);

                //TODO Define getPassword function in Data Handler after new api
                String url = COURSE_PAGE_SLOTS_URL;
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("crs_slots", response);
                        if (response != null&&!response.equals("null")&&!response.equals("")) {
                            ParseCoursePage parseCoursePage = new ParseCoursePage(response, ParseCoursePage.IS_SLOTS);
                            parseCoursePage.parse();
                            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
                            parcel.setRETURN_PARCEL_OBJECT(parseCoursePage);
                            mListener.onRequestCompleted(parcel, COURSE_PAGE_SLOTS_CODE);
                        } else {
                            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                            mListener.onErrorRequest(parcel, COURSE_PAGE_SLOTS_CODE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                        mListener.onErrorRequest(parcel, COURSE_PAGE_SLOTS_CODE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("regNo", dataHandler.getRegNo());
                        paramsMap.put("psswd", dataHandler.getPassword());
                        paramsMap.put("crs", courseId);
                        Log.i(TAG, "getParams: "+paramsMap.toString());
                        return paramsMap;

                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        ConnectAPI.DEFAULT_MAX_TRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                appController.addToRequestQueue(stringRequest, "coursePageSlotRequest");

        } else {
            return;
        }
    }

    private void getCoursePageFaculties(final String courseId, final String slotId) {
        if (mListener != null) {
            mListener.onRequestInitiated(COURSE_PAGE_FACULTIES_CODE);
            //TODO Define getPassword function in Data Handler after new api

                String url = COURSE_PAGE_FACULTIES_URL;
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("crs_faculties", response);
                        if (response != null&&!response.equals("null")&&!response.equals("")) {
                            ParseCoursePage parseCoursePage = new ParseCoursePage(response, ParseCoursePage.IS_FACULTIES);
                            parseCoursePage.parse();
                            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
                            parcel.setRETURN_PARCEL_OBJECT(parseCoursePage);
                            mListener.onRequestCompleted(parcel, COURSE_PAGE_FACULTIES_CODE);
                        } else {
                            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                            mListener.onErrorRequest(parcel, COURSE_PAGE_FACULTIES_CODE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.i(TAG, "onErrorResponse: "+error.getCause());
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                        mListener.onErrorRequest(parcel, COURSE_PAGE_FACULTIES_CODE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("regNo", dataHandler.getRegNo());
                        paramsMap.put("psswd", dataHandler.getPassword());
                        paramsMap.put("crs", courseId);
                        paramsMap.put("slt", slotId);
                        Log.i(TAG, "getParams: "+courseId+" "+slotId);
                        return paramsMap;
                    }



                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        ConnectAPI.DEFAULT_MAX_TRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                appController.addToRequestQueue(stringRequest, "coursePageFacultiesRequest");

        } else {
            return;
        }
    }

    private void getCoursePageUploads(final String courseId, final String slotId, final String facId) {
        if (mListener != null) {
            mListener.onRequestInitiated(COURSE_PAGE_UPLOADS_CODE);

                //TODO Define getPassword function in Data Handler after new api
                String url = COURSE_PAGE_UPLOADS_URL;
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("crs_uploads", response);
                        if (response != null&&!response.equals("null")&&!response.equals("")) {
                            ParseCoursePage parseCoursePage = new ParseCoursePage(response, ParseCoursePage.IS_UPLOADS);
                            parseCoursePage.parse();
                            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
                            parcel.setRETURN_PARCEL_OBJECT(parseCoursePage);
                            mListener.onRequestCompleted(parcel, COURSE_PAGE_UPLOADS_CODE);
                        } else {
                            ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                            mListener.onErrorRequest(parcel, COURSE_PAGE_UPLOADS_CODE);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                        mListener.onErrorRequest(parcel, COURSE_PAGE_UPLOADS_CODE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("regNo", dataHandler.getRegNo());
                        paramsMap.put("psswd", dataHandler.getPassword());
                        paramsMap.put("crs", courseId);
                        paramsMap.put("slt", slotId);
                        paramsMap.put("fac", facId);
                        return paramsMap;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        ConnectAPI.DEFAULT_MAX_TRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                appController.addToRequestQueue(stringRequest, "coursePageUploadsRequest");

        } else {
            return;
        }
    }


    public void setOnRequestListener(RequestListener listener) {
        this.mListener = listener;
    }

    public void changeOnRequestListener(RequestListener listener) {
        this.mListener = listener;
    }



    public interface RequestListener {

        public void onRequestInitiated(int code);

        public void onRequestCompleted(ReturnParcel parcel, int code);

        public void onErrorRequest(ReturnParcel parcel, int code);

    }


}
