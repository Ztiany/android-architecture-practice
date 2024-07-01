package me.ztiany.wan.sample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.base.fragment.tool.commit
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import me.ztiany.wan.sample.presentation.epoxy.FeedFragment
import me.ztiany.wan.sample.presentation.mvi.MVIListFragment
import me.ztiany.wan.sample.presentation.paging3.SquareFragment
import me.ztiany.wan.sample.presentation.segment1.SegmentedArticleList1Fragment
import me.ztiany.wan.sample.presentation.state.SimpleStateFragment
import javax.inject.Inject

@ActivityScoped
class SampleInternalNavigator @Inject constructor(
    @ActivityContext context: Context,
) {

    private val activity = context as AppCompatActivity

    fun showEpoxyList() {
        activity.supportFragmentManager.commit {
            addToStack(fragment = FeedFragment())
        }
    }

    fun showEpoxyMVIList() {
        activity.supportFragmentManager.commit {
            addToStack(fragment = MVIListFragment())
        }
    }

    fun showPaging3List() {
        activity.supportFragmentManager.commit {
            addToStack(fragment = SquareFragment())
        }
    }

    fun showSegment1List() {
        activity.supportFragmentManager.commit {
            addToStack(fragment = SegmentedArticleList1Fragment())
        }
    }

    fun showStatePage() {
        activity.supportFragmentManager.commit {
            addToStack(fragment = SimpleStateFragment())
        }
    }

}