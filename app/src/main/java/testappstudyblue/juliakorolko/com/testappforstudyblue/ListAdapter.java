package testappstudyblue.juliakorolko.com.testappforstudyblue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

/**
 * Created by juliakorolko on 3/17/17.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private List<Repo> data = Collections.emptyList();

    public ListAdapter(Context context, List<Repo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Repo current = data.get(position);
            itemViewHolder.name.setText(current.getName());
            itemViewHolder.updatedAt.setText(context.getResources().getString(R.string.updated_on) +
                    Utils.getDate(current.getUpdatedAt()));
            itemViewHolder.language.setText(current.getLanguage());
            itemViewHolder.id.setText(context.getResources().getString(R.string.repo_id) + current.getId());
            itemViewHolder.ownerLogin.setText(current.getOwnerLogin());
            Picasso.with(context)
                    .load(current.getAvatarUrl())
                    .into(itemViewHolder.avatar);
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.title.setText(context.getResources().getString(R.string.header_title));
            headerHolder.description.setText(context.getResources().getString(R.string.header_description));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView avatar;
        private TextView name;
        private TextView language;
        private TextView updatedAt;
        private TextView id;
        private TextView ownerLogin;
        Context context;

        public ItemViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar_image);
            name = (TextView) itemView.findViewById(R.id.name);
            language = (TextView) itemView.findViewById(R.id.language);
            updatedAt = (TextView) itemView.findViewById(R.id.updated_at);
            id = (TextView) itemView.findViewById(R.id.id_number);
            ownerLogin = (TextView) itemView.findViewById(R.id.owner_login);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Repo current = data.get(getAdapterPosition());
            String cloneUrl = current.getCloneUrl();
            Intent openRepoPage = new Intent(Intent.ACTION_VIEW);
            openRepoPage.setData(Uri.parse(cloneUrl));
            context.startActivity(openRepoPage);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private ImageView headerIcon;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.header_title);
            description = (TextView) itemView.findViewById(R.id.header_text);
            headerIcon = (ImageView) itemView.findViewById(R.id.header_icon);
        }
    }
}
