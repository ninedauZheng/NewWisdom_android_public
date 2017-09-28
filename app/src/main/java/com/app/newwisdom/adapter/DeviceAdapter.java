package com.app.newwisdom.adapter;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.newwisdom.R;
import com.app.newwisdom.activity.BaseActivity;
import com.app.newwisdom.activity.WIFISettingActivity;
import com.app.newwisdom.adapter.view.DeviceView;
import com.app.newwisdom.callback.OkHttpCallBack;
import com.app.newwisdom.callback.OnDialogButtonClickCallback;
import com.app.newwisdom.manager.DeviceManager;
import com.app.newwisdom.manager.HttpManager;
import com.app.newwisdom.model.item.DeviceItem;
import com.app.newwisdom.util.AppUtils;
import com.app.newwisdom.util.ScreenParams;
import com.app.newwisdom.view.view.CommonDialog;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.app.newwisdom.receiver.BroadcastInfo.DATA_CHANGE;

/**
 * Created by ninedau_zheng on 2017/8/2.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceView> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    public static final int TITLE_VIEW_SMALL = 2;
    public static final int TITLE_VIEW_BIG = 1;
    public static final int COMMON_PARENT_VIEW = 0;
    public static final int COMMON_CHILD_VIEW = 3;
    private BaseActivity context;
    private List<DeviceItem> list;

    public DeviceAdapter(BaseActivity context, List<DeviceItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public DeviceView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        ViewGroup.LayoutParams param;
        switch (viewType) {
            case COMMON_PARENT_VIEW:
                view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
                break;
            case COMMON_CHILD_VIEW:
                view = LayoutInflater.from(context).inflate(R.layout.item_child, parent, false);
                ((RadioGroup) view.findViewById(R.id.rg)).setOnCheckedChangeListener(this);
                break;
            case TITLE_VIEW_BIG:
                view = LayoutInflater.from(context).inflate(R.layout.item_device_title, parent, false);
                param = view.getLayoutParams();
                param.height = ScreenParams.getInstance(context).getFormatHeight(112);
                view.setLayoutParams(param);
                break;
            case TITLE_VIEW_SMALL:
                view = LayoutInflater.from(context).inflate(R.layout.item_device_title, parent, false);
                param = view.getLayoutParams();
                param.height = ScreenParams.getInstance(context).getFormatHeight(68);
                view.setLayoutParams(param);
                break;
        }
        DeviceView dv = new DeviceView(view);
        return dv;
    }

    @Override
    public void onBindViewHolder(DeviceView holder, int position) {
        holder.bindView(position, list.get(position), getOnclickListener(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public View.OnClickListener getOnclickListener(final int position) {
        View.OnClickListener listener = null;

        switch (position) {
            case 1:
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, WIFISettingActivity.class));
                    }
                };
                break;
            case 2:
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeOthers(position);
                        DeviceItem item = list.get(position);
                        if (item.isOpen) {
                            list.remove(position + 1);
                            notifyItemRemoved(position + 1);
                            item.isOpen = false;
                        } else {
                            list.add(position + 1, item.childItem);
                            notifyItemInserted(position + 1);
                            item.isOpen = true;
//                            DeviceItem itemss = list.get(position);
                        }
                    }
                };
                break;
            case 4:
                listener = createListener(context.getResources().getString(R.string.unbind), new OnDialogButtonClickCallback() {
                    @Override
                    public void onDialogCallback(boolean isPositive) {
                        if (isPositive) {
                            //解除绑定操作
                            AppUtils.removeSPIMEI(context);
                            AppUtils.removeIccid(context, DeviceManager.getInstance().getRouter().iccid);
                            DeviceManager.getInstance().clearRouter();
                            context.sendBroadcast(new Intent(DATA_CHANGE));
                            Toast.makeText(context, "设备解绑成功", Toast.LENGTH_LONG).show();
                            context.finish();
                        }
                    }

                });
                break;
            case 5:
                listener = createListener(context.getResources().getString(R.string.reset), new OnDialogButtonClickCallback() {
                    @Override
                    public void onDialogCallback(boolean isPositive) {
                        if (isPositive) {
                            HttpManager hm = HttpManager.getInstance(context);

                            hm.resetDevice(context, AppUtils.getSPIMEI(context), new OkHttpCallBack() {
                                @Override
                                public void onFailure(Call call, IOException exception) {
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (response.code() == 200) {
                                                DeviceManager.getInstance().setConnectTrueRouter(false);
                                                Toast.makeText(context, "恢复出厂成功", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    }

                });
                break;
            case 3:
            case 7:
                listener = createListener(true, context.getResources().getString(R.string.no_ok), new OnDialogButtonClickCallback() {
                    @Override
                    public void onDialogCallback(boolean isPositive) {

                    }
                });
                break;
        }
        return listener;
    }

    @Override
    public void onClick(View v) {

    }

    private void removeOthers(int p) {
        for (int i = 0; i < list.size(); i++) {
            DeviceItem item = list.get(i);
            if (item.childItem == null) continue;
            if (item.isOpen && i != p) {
                list.remove(i + 1);
                notifyItemRemoved(i + 1);
                item.isOpen = false;
            }
        }
    }

    private View.OnClickListener createListener(final boolean isNotice, final String value, final OnDialogButtonClickCallback callback) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOthers(0);
                CommonDialog dialog =
                        new CommonDialog(context);
                if (isNotice) {
                    dialog.setCommonNotice(value);
                } else {
                    dialog.setCommonTips(value);
                }
                dialog.setCallback(callback);
                dialog.show();
            }
        };
        return listener;
    }

    private View.OnClickListener createListener(String value, OnDialogButtonClickCallback callback) {
        return createListener(false, value, callback);
    }


    @Override
    public void onCheckedChanged(final RadioGroup group, @IdRes final int checkedId) {
        CommonDialog cd = new CommonDialog(context);

        final RadioButton rb0 = (RadioButton) group.getChildAt(0);
        final RadioButton rb1 = (RadioButton) group.getChildAt(1);
        if (!rb0.isChecked() && !rb1.isChecked()) return;
        Log.i("ninedau", "rb= " + rb0.isChecked() + " , rb1=" + rb1.isChecked());
        switch (checkedId) {
            case R.id.rb_restart:

                cd.setCommonTips(context.getResources().getString(R.string.restart));

                break;
            case R.id.rb_shut_down:
                cd.setCommonTips(context.getResources().getString(R.string.shutdown));
                break;
        }
        cd.setCallback(new OnDialogButtonClickCallback() {
            @Override
            public void onDialogCallback(boolean isPositive) {
                if (isPositive) {
                    gotoDoRouter(checkedId);
                } else {
                    group.clearCheck();
                }
            }

        });
        cd.show();
    }

    OkHttpCallBack operationCallback = new OkHttpCallBack() {
        @Override
        public void onFailure(Call call, IOException exception) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.code()==200){
                DeviceManager.getInstance().setConnectTrueRouter(false);
            }
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "操作成功", Toast.LENGTH_LONG).show();

                }
            });
        }
    };

    private void gotoDoRouter(int id) {
        HttpManager manager = HttpManager.getInstance(context);

        switch (id) {
            case R.id.rb_restart:
                manager.restartDevice(context, AppUtils.getSPIMEI(context), operationCallback);
                break;
            case R.id.rb_shut_down:
                manager.shutDownDevice(context, AppUtils.getSPIMEI(context), operationCallback);
                break;
        }
    }
}

