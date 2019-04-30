package com.ubao.techexcel.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ubao.techexcel.R;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.help.SideBarSortHelp;
import com.ubao.techexcel.info.Customer;
import com.ubao.techexcel.tool.PinyinComparator;

import java.util.Collections;
import java.util.List;

public class CustomerAdapter extends CommonAdapter<Customer> {

    private List<Customer> list = null;
    private Context mContext;
//    public ImageLoader imageLoader;

    public CustomerAdapter(Context mContext, List<Customer> list2) {
        super(mContext, list2);
        this.mContext = mContext;
        AddRobetinfo(list2);
    }

    private void AddRobetinfo(List<Customer> list2) {
        SortCustomers(list2);
        Customer cus = new Customer();
        cus.setName(AppConfig.RobotName);
        cus.setSortLetters(AppConfig.Robot);
        this.list = list2;
        this.list.add(0, cus);
    }

    public void updateListView(List<Customer> list2) {
        AddRobetinfo(list2);
        updateAdapter(list);
    }


    public void updateListView2(List<Customer> list2) {
        this.list = list2;
        updateAdapter(list);
    }

    @SuppressLint("NewApi")
    @Override
    public void convert(ViewHolder holder, Customer customer, int position) {
        /*String hConcerns = "";
		ArrayList<CommonUse> cu_list = new ArrayList<CommonUse>();
		cu_list = customer.getHealthConcerns();
		for (int i = 0; i < cu_list.size(); i++) {
			CommonUse commonuse = cu_list.get(i);
			hConcerns += commonuse.getName();
			if (i != (cu_list.size() - 1)) {
				hConcerns += "、";
			}
		}*/

        holder.setText(R.id.tv_name, customer.getName())
                .setText(R.id.tv_peertimeid, "null")
                .setText(R.id.tv_sort, customer.getSortLetters());
//				.setText(R.id.tv_gender,customer.getSex().equals("2")? "女" : "男")
//				.setText(R.id.tv_age, customer.getAge() + "岁")
				/*.setText(R.id.tv_serviceTimes,
						"服务次数：" + customer.getServiceCount() + "次")*/
//				.setText(R.id.tv_distance, customer.getDistance())
//				.setText(R.id.tv_description, hConcerns)
//				.setText(R.id.tv_remark, customer.getPersonalComment())
//				.setText(R.id.tv_phone, customer.getPhone())
//				.setText(R.id.tv_address, customer.getCurrentPosition())
//				.setViewVisible(R.id.img_crown, customer.isCrown()?View.VISIBLE:View.GONE)
//				.setViewVisible(R.id.img_new, customer.isNew()?View.VISIBLE:View.GONE);
		
		/*if(customer.getSex().equals("0")){
			holder.setText(R.id.tv_gender,"");
		}*/
		/*Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.location);
		RoundImageDrawable Rid = new RoundImageDrawable(bmp);
		holder.setImageDrawable(R.id.img_head, Rid).setImageDrawable(
				R.id.img_head, Rid);*/

        final SimpleDraweeView img = holder.getView(R.id.img_head);
        String url = customer.getUrl();
        Uri imageUri = null;
        if(!TextUtils.isEmpty(url)) {
            imageUri = Uri.parse(url);
        }
        img.setImageURI(imageUri);

		/*final CircleImageView img = holder.getView(R.id.img_head);
		String url = customer.getUrl();
		if (null == url || url.length() < 1) {
			img.setImageResource(R.drawable.hello);			
		}else{
			final String imgurl = customer.getUrl();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					imageLoader.DisplayImage(imgurl, img);
				}
			}, 100);
		}*/
		

		/*LinearLayout myLayout = holder.getView(R.id.lin_problem);
		myLayout.removeAllViews();
		int size = 0;
		if(customer.getFocusPoints() != null){
			size = (customer.getFocusPoints().size() > 3 ? 3 : customer
				.getFocusPoints().size());
		}
		for (int i = 0; i < size; i++) {
			String problem = customer.getFocusPoints().get(i);
			TextView tv_problem = new TextView(mContext);
			tv_problem.setText(problem + "");
			tv_problem.setBackground(mContext.getResources().getDrawable(R.drawable.contact_tv_problem));
			tv_problem.setPadding(10, 0, 10, 0);
//			tv_problem.setBackgroundColor(mContext.getResources().getColor(R.color.green));
			tv_problem.setGravity(Gravity.CENTER);
			tv_problem.setTextColor(mContext.getResources().getColor(R.color.white));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 10, 0);
			                      
			tv_problem.setLayoutParams(lp);  
			myLayout.addView ( tv_problem ) ;
		}*/
        if (position != 0) {
            int sectionVisible = SideBarSortHelp.getPositionForSection(list,
                    customer.getSortLetters().charAt(0));
            if (sectionVisible == position) {
                holder.setViewVisible(R.id.tv_sort, View.VISIBLE);
            } else {
                holder.setViewVisible(R.id.tv_sort, View.GONE);
            }
        } else {
            holder.setViewVisible(R.id.tv_sort, View.VISIBLE);
        }


    }

    @Override
    public int getLayout(int position) {
        // TODO Auto-generated method stub
        return R.layout.customer_item;
    }

    private void SortCustomers(List<Customer> list2) {
        Collections.sort(list2, new PinyinComparator());
    }

}
