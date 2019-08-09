package com.example.owner.mystarlive;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.webrtc.ContextUtils.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AutoScrollViewPager autoViewPager;
    /*라이브 리스트뷰관련 선언*/
    private LiveAdapter liveAdapter;
    private RecyclerView liverecyclerView;
    ArrayList<Live> LiveRecyclerArrayList;

    /*VOD 리스트뷰관련 선언*/
    ArrayList<Vod> VodRecyclerArrayList;
    private VodAdapter vodAdapter;
    private RecyclerView vodrecyclerView;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        /*상단에 뜨는 광고이미지*/
        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("http://www.earlyadopter.co.kr/wp-content/uploads/2019/06/netmarble-bts-world-teaser-2-1024x576.jpg");
        data.add("https://i.ytimg.com/vi/UMoD_57GlNo/maxresdefault.jpg");
        data.add("https://image.fmkorea.com/files/attach/new/20180730/3655109/1161611/1184974504/cbdb3501bad17fc939181301203130a5.png");
        data.add("https://exo-jp.net/common/images/update/slide_20180509cbx.jpg");

        autoViewPager = (AutoScrollViewPager)view.findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getActivity(), data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(5000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

        vodrecyclerView = view.findViewById(R.id.VODStreaming);
        liverecyclerView = view.findViewById(R.id.LiveStreaming);

        Livelist();
        Vodlist();

        return view;
    }



    /*retrofit을 이용해서 서버에서 Live목록을 json형태로 가져온다*/
    private void Livelist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getlivelist();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        Log.e("jsonresponse", jsonresponse);

                        writeLive(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /*Recyclerview로 가져온 Live목록을 뿌려준다.*/
    private void writeLive(String response){

        try {

                JSONArray liveArray  = new JSONArray(response);
                Log.e("dataArray", String.valueOf(liveArray));
            LiveRecyclerArrayList = new ArrayList<Live>();
                for (int i = 0; i < liveArray.length(); i++) {

                    Live modelRecycler = new Live();
                    JSONObject dataobj = liveArray.getJSONObject(i);
                    Log.e("dataobj", String.valueOf(dataobj));

                    Log.e("dataobj.getString : ", dataobj.getString("live_id"));
                    modelRecycler.setImgURL(dataobj.getString("live_image"));
                    modelRecycler.setliveid(dataobj.getString("live_id"));
                    modelRecycler.setlivetitle(dataobj.getString("live_title"));

                    LiveRecyclerArrayList.add(modelRecycler);

                }

            liveAdapter = new LiveAdapter(getContext(),LiveRecyclerArrayList);
            liverecyclerView.setAdapter(liveAdapter);
            liverecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*retrofit을 이용해서 서버에서 Vod목록을 json형태로 가져온다*/
    private void Vodlist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getvoidlist();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("VODResponsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        Log.e("VODjsonresponse", jsonresponse);

                        writeVod(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /*Recyclerview로 가져온 Vod목록을 뿌려준다.*/
    private void writeVod(String response){

        try {

            JSONArray vodArray  = new JSONArray(response);
            Log.e("vodArray", String.valueOf(vodArray));
            VodRecyclerArrayList = new ArrayList<Vod>();
            for (int i = 0; i < vodArray.length(); i++) {

                Vod vodRecycler = new Vod();
                JSONObject vodobj = vodArray.getJSONObject(i);
                Log.e("dataobj", String.valueOf(vodobj));
                String id = "유저 " + vodobj.getString("vod_id");
                Log.e("dataobjid :", id);

                Log.e("dataobj.getString : ", vodobj.getString("vod_id"));
                vodRecycler.setImgURL(vodobj.getString("vod_image"));
                vodRecycler.setvodid(id);
                vodRecycler.setvodtitle(vodobj.getString("vod_title"));

                VodRecyclerArrayList.add(vodRecycler);

            }

            vodAdapter = new VodAdapter(getContext(),VodRecyclerArrayList);
            vodrecyclerView.setAdapter(vodAdapter);
            vodrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
