package com.example.pooja.pk_electronics.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pooja.pk_electronics.R;
import com.example.pooja.pk_electronics.activity.DashboardActivity;
import com.example.pooja.pk_electronics.activity.ItemContentActivity;
import com.example.pooja.pk_electronics.activity.UpdateItemActivity;
import com.example.pooja.pk_electronics.database.DBHelper;
import com.example.pooja.pk_electronics.model.DashboardModel;
import com.example.pooja.pk_electronics.service.SendRequestServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pooja on 5/15/2017.
 */

public class DashboardCustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DashboardModel> arrayList;
    private ArrayList<DashboardModel> arraylistFilter;
    int id;

    public DashboardCustomAdapter(DashboardActivity dashboardActivity, ArrayList<DashboardModel> arrayList) {
        this.context = dashboardActivity;
        this.arrayList = arrayList;
        this.arraylistFilter = new ArrayList<DashboardModel>();
        this.arraylistFilter.addAll(arrayList);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHoder viewHoder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dashboard_layout, parent, false);

            viewHoder = new ViewHoder();

            viewHoder.lin = (RelativeLayout) convertView.findViewById(R.id.rel);
            viewHoder.id = (TextView) convertView.findViewById(R.id.textView);
            viewHoder.product = (TextView) convertView.findViewById(R.id.textView2);
            viewHoder.price1 = (TextView) convertView.findViewById(R.id.textView3);
            viewHoder.price2 = (TextView) convertView.findViewById(R.id.textView4);
            viewHoder.price3 = (TextView) convertView.findViewById(R.id.textView5);
            viewHoder.updated_on = (TextView) convertView.findViewById(R.id.textView6);

            convertView.setTag(viewHoder);


        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        final DashboardModel beanClass = (DashboardModel) getItem(position);
        viewHoder.product.setText(beanClass.getPname());
        viewHoder.price1.setText(beanClass.getPrice1()+" Rs.");
        viewHoder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String username = preferences.getString("username", "0");

                Intent in = new Intent(context, ItemContentActivity.class);
                in.putExtra("pname", beanClass.getPname());
                in.putExtra("price1", beanClass.getPrice1());
                in.putExtra("price2", beanClass.getPrice2());
                in.putExtra("price3", beanClass.getPrice3());
                in.putExtra("updatedOn", beanClass.getUpdatedOn());
                in.putExtra("updatedBy", username);
                context.startActivity(in);
            }
        });
        viewHoder.lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                id = beanClass.getId();
                final String myId = String.valueOf(id);
                final String pnanme = beanClass.getPname();
                final String price1 = beanClass.getPrice1();
                final String price2 = beanClass.getPrice2();
                final String price3 = beanClass.getPrice3();
                final String updatedon = beanClass.getUpdatedOn();


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_demo);
                dialog.setTitle("Take an action");
                Button edit = (Button) dialog.findViewById(R.id.edit);
                Button delete = (Button) dialog.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Are you sure,You wanted to make delete");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        new DataFromServer().execute();
                                        dialog.dismiss();
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //     dialog.dismiss();
                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UpdateItemActivity.class);
                        intent.putExtra("idSend", myId);
                        intent.putExtra("pnanme", pnanme);
                        intent.putExtra("price1", price1);
                        intent.putExtra("price2", price2);
                        intent.putExtra("price3", price3);
                        intent.putExtra("updatedon", updatedon);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.show();


                return false;
            }
        });
        return convertView;
    }

    private class ViewHoder {
        RelativeLayout lin;
        TextView id;
        TextView product;
        TextView price1;
        TextView price2;
        TextView price3;
        TextView updated_on;
    }

    private class DataFromServer extends AsyncTask<Void, Void, Void> {
        HashMap<String, String> nameValuePairs;
        String responsefromserver = null;

        @Override
        protected Void doInBackground(Void... voids) {
            nameValuePairs = new HashMap<String, String>();
            nameValuePairs.put("id", String.valueOf(id));
            SendRequestServer req = new SendRequestServer();
            String url1 = "delete_item.php";
            responsefromserver = req.requestSender(url1, nameValuePairs, context);


            return null;
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(Void unused) {
            String aa = responsefromserver;
            if (responsefromserver.equals("")) {

                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                // logginFromMobile();
            } else {
                DBHelper bn = new DBHelper(context);
                bn.clearSingeOrders(id);
                Intent in = new Intent(context, DashboardActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(in);
                Toast.makeText(context, "delete  success", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(arraylistFilter);
        } else {
            for (DashboardModel wp : arraylistFilter) {
                if (wp.getPname().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    arrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
