package com.example.owner.mystarlive;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owner.mystarlive.viewer.Goods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.webrtc.ContextUtils.getApplicationContext;


/**
옥션에서 크롤링한 굿즈아이템을 goods.php로 불러서 리사이클러뷰로 띄워준다.
 */
public class GoodsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /*굿즈관련 선언
    * 어댑터, 리사이클러뷰, 어레이리스트 선언*/
    private GoodsAdapter goodsAdapter;
    private RecyclerView GoodsRecyclerView;
    ArrayList<Goods> GoodsRecyclerArrayList;

    private OnFragmentInteractionListener mListener;

    public GoodsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoodsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoodsFragment newInstance(String param1, String param2) {
        GoodsFragment fragment = new GoodsFragment();
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
        View view=inflater.inflate(R.layout.fragment_goods, container, false);


        GoodsRecyclerView = view.findViewById(R.id.GoodsRecyclerView);

        Goodslist();
        return view;
    }

    /*retrofit을 이용해서 서버에서 Goods목록을 json형태로 가져온다*/
    private void Goodslist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);

        Call<String> call = api.getgoodslist();

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

                        writeGoods(jsonresponse);

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

    /*Recyclerview로 가져온 goods목록을 뿌려준다.*/
    private void writeGoods(String response){

        try {

            JSONArray goodsArray  = new JSONArray(response);
            Log.e("dataArray", String.valueOf(goodsArray));
            GoodsRecyclerArrayList = new ArrayList<Goods>();
            for (int i = 0; i < goodsArray.length(); i++) {

                Goods modelRecycler = new Goods();
                JSONObject dataobj = goodsArray.getJSONObject(i);
                Log.e("Goods : ", String.valueOf(dataobj));

                Log.e("dataobj.getString : ", dataobj.getString("goods"));

                String str = dataobj.getString("goods");
                StringTokenizer tokenizer = new StringTokenizer(str, ",");

                /*받아온 goods데이터 '단위로 자르기
                String str = dataobj.getString("goods");
                String[] array = str.split("'");*/

//출력

                    String no = tokenizer.nextToken();
                    String title = tokenizer.nextToken();
                    String price = tokenizer.nextToken();
                    String url = tokenizer.nextToken();
                    String mtitle = tokenizer.nextToken();
                    String st = tokenizer.nextToken();
                    Log.e("no : ", no);
                    Log.e("title : ", title.substring(2,title.length()-1));
                    Log.e("price : ", price.substring(2,price.length()-1)+"원");
                    Log.e("url : ", url.substring(2,url.length()-1));
                    Log.e("mtitle : ", mtitle);
                    Log.e("st : ", st);

                    modelRecycler.setImgURL(url.substring(1,url.length()-1));
                    modelRecycler.setgoodstitle(title.substring(2,title.length()-1));
                    modelRecycler.setprice(price.substring(1,price.length()-1)+"원");
                    GoodsRecyclerArrayList.add(modelRecycler);






            }

            goodsAdapter = new GoodsAdapter(getContext(),GoodsRecyclerArrayList);
            GoodsRecyclerView.setAdapter(goodsAdapter);
            GoodsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));



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
