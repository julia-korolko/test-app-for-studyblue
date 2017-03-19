package testappstudyblue.juliakorolko.com.testappforstudyblue;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemsList;
    private ListAdapter adapter;
    private List<Repo> data;
    private LinearLayout noInternet;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noInternet = (LinearLayout) findViewById(R.id.no_connection_view_container);
        checkConnection();
    }

    public void checkConnection() {
        boolean isConnected;
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            noInternet.setVisibility(View.GONE);
            data = new ArrayList<>();
            getReposData();
            itemsList = (RecyclerView) findViewById(R.id.items_list);
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    public void getReposData() {

        Requests service = Requests.retrofit.create(Requests.class);
        Call<ResponseBody> getRepos = service.getUserData();
        getRepos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonElement jsonElement = new JsonParser().parse(response.body().string());
                        JsonArray array = jsonElement.getAsJsonArray();
                        for (JsonElement obj : array) {
                            Repo repo = new Gson().fromJson(obj, Repo.class);
                            JsonObject temp = obj.getAsJsonObject();
                            repo.setUpdatedAt(temp.get("updated_at").getAsString());
                            repo.setCloneUrl(temp.get("clone_url").getAsString());
                            JsonObject owner = temp.get("owner").getAsJsonObject();
                            repo.setAvatarUrl(owner.get("avatar_url").getAsString());
                            repo.setOwnerLogin(owner.get("login").getAsString());
                            data.add(repo);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter = new ListAdapter(MainActivity.this, data);
                    itemsList.setAdapter(adapter);
                    itemsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    itemsList.addItemDecoration(
                            new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(),
                                    R.drawable.item_divider)));
                } else {
                    Log.d(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void retryConnection(View view) {
        checkConnection();
    }

    class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public DividerItemDecoration(Drawable divider) {
            this.mDivider = divider;
        }

        @Override
        public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
    }
}
