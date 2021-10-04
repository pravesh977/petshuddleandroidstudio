package android.portfolio.petshuddle.Adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> myFragmentsList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setFragments(ArrayList<Fragment> myFragmentsList) {
        this.myFragmentsList = myFragmentsList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return myFragmentsList.get(position);
    }

    @Override
    public int getItemCount() {
        return myFragmentsList.size();
    }
}
