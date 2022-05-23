package com.doan.mp3music.ui.screen.main;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.doan.mp3music.api.ApiBuilder;
import com.doan.mp3music.ui.base.BaseViewModel;
import com.doan.mp3music.ui.screen.album.AlbumFragment;
import com.doan.mp3music.ui.screen.artist.ArtistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.doan.mp3music.R;
import com.doan.mp3music.databinding.ActivityMainBinding;
import com.doan.mp3music.service.MP3Service;
import com.doan.mp3music.ui.base.BaseActivity;
import com.doan.mp3music.ui.base.BaseFragment;
import com.doan.mp3music.ui.screen.favorite.FavoriteFragment;
import com.doan.mp3music.ui.screen.online.OnlineFragment;
import com.doan.mp3music.ui.screen.song.SongFragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> implements SearchView.OnQueryTextListener {
    public static boolean isVip = false;
    private OnlineFragment fmOnline = new OnlineFragment();
    private FavoriteFragment fmFavorite = new FavoriteFragment();
    private SongFragment fmMyMusic = new SongFragment();
    private MainFragment fmMain = new MainFragment();
    private ArtistFragment fmArtist = new ArtistFragment();
    private AlbumFragment fmAlbum = new AlbumFragment();

    private BaseFragment fmShow;
    private MP3Service service;
    public Menu menu;

    @Override
    protected Class<BaseViewModel> getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected void init() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        requestPermission(permissions, new PermissionListener() {
            @Override
            public void onResult(boolean isGranted) {
                if (isGranted) {
                    showFragment(fmMain);
                    Intent intent = new Intent(MainActivity.this,
                            MP3Service.class);
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                } else {
                    finish();
                }
            }
        });
    }

    private void checkSub() {
        String device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        ApiBuilder.getInstance().checkSubscription(device).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                menu.findItem(R.id.menu_vip).setVisible(response.code() == 200);
                menu.findItem(R.id.menu_buy_vip).setVisible(response.code() != 200);
                isVip = response.code() == 200;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MP3Service.MP3Binder mp3Binder = (MP3Service.MP3Binder) binder;
            service = mp3Binder.getService();
            binding.controllerView.setService(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void showFragment(BaseFragment fmShow) {
        if (this.fmShow != null && this.fmShow.getBaseAdapter() != null) {
            this.fmShow.getBaseAdapter().getFilter().filter("");
        }
        this.fmShow = fmShow;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.container, fmShow);
        if(!(fmShow instanceof MainFragment)) {
            transaction.addToBackStack(fmShow.getClass().getName());
        }
        transaction.commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.menu_vip).setVisible(false);
        this.menu = menu;
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        checkSub();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_buy_vip) {
            buyVid();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public MP3Service getService() {
        return service;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        this.fmShow.getBaseAdapter().getFilter().filter(s);
        return true;
    }

    private void buyVid() {
        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
            Log.e(getClass().getName(), billingResult.getResponseCode() + "");
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                String device = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                ApiBuilder.getInstance().addSubscription(device).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            checkSub();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        };
        BillingClient billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    List<String> skuList = new ArrayList<>();
                    skuList.add("android.test.purchased");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    // Process the result.
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                    int responseCode = billingClient.launchBillingFlow(MainActivity.this, billingFlowParams).getResponseCode();
                                }
                            });


                }
            }
            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    public OnlineFragment getFmOnline() {
        return fmOnline;
    }

    public FavoriteFragment getFmFavorite() {
        return fmFavorite;
    }

    public SongFragment getFmMyMusic() {
        return fmMyMusic;
    }

    public MainFragment getFmMain() {
        return fmMain;
    }

    public AlbumFragment getFmAlbum() {
        return fmAlbum;
    }

    public ArtistFragment getFmArtist() {
        return fmArtist;
    }
}
