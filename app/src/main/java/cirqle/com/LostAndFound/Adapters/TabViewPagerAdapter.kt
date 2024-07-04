package cirqle.com.LostAndFound.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import cirqle.com.LostAndFound.FoundItemFragment
import cirqle.com.LostAndFound.LostItemFragment

class TabViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2 // Number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FoundItemFragment()
            1 -> LostItemFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
