package example.kotlinreport.multiplatform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import example.kotlinreport.multiplatform.adapter.UserAdapter
import example.kotlinreport.shared.common.paginator.AndroidPaginator
import example.kotlinreport.shared.common.paginator.Paginator
import example.kotlinreport.shared.common.recyclerview.listener.OnScrollLoadMoreRecyclerViewListener
import example.kotlinreport.shared.config.AppConfig
import example.kotlinreport.shared.db.AndroidDatabaseManager
import example.kotlinreport.shared.db.DatabaseManager
import example.kotlinreport.shared.model.SexType
import example.kotlinreport.shared.model.User
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mDatabaseManager: DatabaseManager;
    private lateinit var mUsers: ArrayList<User>
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mOnLoadMore: OnScrollLoadMoreRecyclerViewListener
    private lateinit var mPaginator: Paginator
    private lateinit var mRecyclerViewUser: RecyclerView
    private lateinit var mSwipeRefreshLayoutMain: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatabaseManager()
        initPaginatorUser()

        initRefresh()
        initRecyclerView()
    }

    private fun onActionDelete(user: User, position: Int) {
        Log.d("onActionUpdate", user.id.toString())
//        DatabaseManager.deleteUser(this, user)
//        mUserAdapter.removeItem(position)
//        Toast.makeText(this, "Deleted user name \"${user.name}\" success", Toast.LENGTH_SHORT).show()
    }

    private fun onActionUpdate(user: User, position: Int) {
        Log.d("onActionUpdate", "id:${user.id} - position:${position}")

//        val intentUpdate = Intent(this, UpdateActivity::class.java)
//        intentUpdate.putExtra("id", user.id)
//        intentUpdate.putExtra("position", position)
//        startActivityForResult(intentUpdate, REQUEST_CODE_UPDATE_ACTIVITY)

    }

    private fun initDatabaseManager() {
        this.mDatabaseManager = AndroidDatabaseManager(this);
    }

    private fun initPaginatorUser() {
        val total: Long = this.mDatabaseManager.countUser()
        Log.d("total", total.toString())
        mPaginator = AndroidPaginator(1, AppConfig.Pagination.PAGE_SIZE, total)
    }

    private fun initRefresh() {
        mSwipeRefreshLayoutMain = findViewById(R.id.swipeRefreshLayoutMain)
        mSwipeRefreshLayoutMain.setOnRefreshListener(this)
    }

    private fun initRecyclerView() {
        mUsers = this.mDatabaseManager.getUserList(mPaginator.getOffset(), mPaginator.mPageSize)
        Log.d("Users", mUsers.size.toString())
        if (mPaginator.hasNextPage()) {
            mPaginator.mPage++
        }

        mUserAdapter = object : UserAdapter(mUsers as ArrayList<User?>) {
            override fun onActionDelete(user: User, position: Int) {
                this@MainActivity.onActionDelete(user, position)
            }

            override fun onActionUpdate(user: User, position: Int) {
                this@MainActivity.onActionUpdate(user, position)
            }
        }
        mOnLoadMore = object : OnScrollLoadMoreRecyclerViewListener(10) {
            override fun onLoadMore() {
                getData()
            }
        }
        mRecyclerViewUser = findViewById(R.id.recyclerViewMainUser)
        mRecyclerViewUser.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 1)
            setHasFixedSize(true)
            adapter = mUserAdapter
            addOnScrollListener(mOnLoadMore)
            // add divider
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }

    }

    fun getData() {
        // accept load more if not refresh
        if (!mSwipeRefreshLayoutMain.isRefreshing) {
            mOnLoadMore.mIsLoading = true
            mUserAdapter.openLoading()
        }

        var hander = Handler()
        hander.postDelayed(Runnable() {
            if (!mSwipeRefreshLayoutMain.isRefreshing) {
                mUserAdapter.closeLoading()
            }
            val users = this.mDatabaseManager.getUserList(mPaginator.getOffset(), mPaginator.mPageSize)
            Log.d("addAllUser", users.size.toString())
            mUserAdapter.addAll(users)

            if (mSwipeRefreshLayoutMain.isRefreshing) {
                mSwipeRefreshLayoutMain.isRefreshing = false
            }

            mOnLoadMore.mIsLoading = false
            if (mPaginator.hasNextPage()) {
                Log.d("mPaginator", mPaginator.toString())
                mOnLoadMore.mHasNextPage = true
                mPaginator.mPage++
            } else {
                mOnLoadMore.mHasNextPage = false
            }
        }, 2000)


    }

    private fun fakeData(): Unit {
        val total: Long = this.mDatabaseManager.countUser()
        var i: Long = 1
        var random: Random = Random()
        while (i < 11) {
            val sexRandom: Int = random.nextInt(2) // [0;1]
            var sex: SexType
            if (sexRandom == SexType.MALE.value) {
                sex = SexType.MALE
            } else {
                sex = SexType.FEMALE
            }
            this.mDatabaseManager.insertUser(
                User(null, "Test ${total + i}", sex, null)
            )
            i++
        }
    }

    override fun onRefresh() {
        mUserAdapter.clear()
        initPaginatorUser()
        Log.d("onRefreshPaginator", "page:${mPaginator.mPage}|total:${mPaginator.mTotal}|offset:${mPaginator.getOffset()}")
        getData()
    }
}