package com.example.sunlianglong.messageServer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.sunlianglong.chatnei.R;
import java.util.List;

/**
 * Created by sun liang long on 2017/10/20.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private int resourceId;
    private String my_ip;

    public MessageAdapter(Context context, int textViewResourceId,
                          List<Message> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message mess = getItem(position); // 获取当前项的实例
        View view;
        //方法三：
        ViewHolder  viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.messageImage = (ImageView) view.findViewById (R.id.message_image);
            viewHolder.messageName = (TextView) view.findViewById (R.id.message_name);
            viewHolder.message_geqian = (TextView) view.findViewById (R.id.geqian_show);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {

            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.messageImage.setImageResource(mess.getImageId());
        viewHolder.messageName.setText(mess.getName());
        viewHolder.message_geqian.setText(mess.getgeqian());
        return view;
    }
    class ViewHolder {
        ImageView messageImage;
        TextView messageName;
        TextView message_geqian;
    }
}
//FruitAdapter重写了父类的一组构造函数，用于将上下文、ListView子项布局的id和数据都传递进来。
// 另外又重写了getView()方法，这个方法在每个子项被滚动到屏幕内的时候会被调用。在getView方法中
// ，首先通过getItem()方法得到当前项的Fruit实例，然后使用LayoutInflater来为这个子项加载我们传
// 入的布局，接着调用View的findViewById()方法分别获取到ImageView和TextView的实例，并分别调用
// 它们的setImageResource()和setText()方法来设置显示的图片和文字，最后将布局返回，这样我们自
// 定义的适配器就完成了。

//方法一的ListView的运行效率是很低的，因为在FruitAdapter的getView()方法中每次都将布局重新加载了
// 一遍，当ListView快速滚动的时候这就会成为性能的瓶颈。

//方法二：可以看到，现在我们在getView()方法中进行了判断，如果convertView为空，则使用LayoutInflater
// 去加载布局，如果不为空则直接对convertView进行重用。这样就大大提高了ListView的运行效率，在快速滚
// 动的时候也可以表现出更好的性能

//方法三：不过，目前我们的这份代码还是可以继续优化的，虽然现在已经不会再重复去加载布局，但是每次在
// getView()方法中还是会调用View的findViewById()方法来获取一次控件的实例。我们可以借助一个ViewHolder
// 来对这部分性能进行优化，
//我们新增了一个内部类ViewHolder，用于对控件的实例进行缓存。当convertView为空的时候，创建一个ViewHolder
// 对象，并将控件的实例都存放在ViewHolder里，然后调用View的setTag()方法，将ViewHolder对象存储在View中。
// 当convertView不为空的时候则调用View的getTag()方法，把ViewHolder重新取出。这样所有控件的实例都缓存在
// 了ViewHolder里，就没有必要每次都通过findViewById()方法来获取控件实例了。
