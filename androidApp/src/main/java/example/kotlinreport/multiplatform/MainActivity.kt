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
import example.kotlinreport.multiplatform.shared.db.UserDao
import example.kotlinreport.multiplatform.shared.common.paginator.AndroidPaginator
import example.kotlinreport.multiplatform.shared.common.paginator.Paginator
import example.kotlinreport.multiplatform.shared.common.recyclerview.listener.OnScrollLoadMoreRecyclerViewListener
import example.kotlinreport.multiplatform.shared.config.AppConfig
import example.kotlinreport.multiplatform.shared.myAppContext
import example.kotlinreport.multiplatform.shared.model.SexType
import example.kotlinreport.multiplatform.shared.model.User
import java.util.*

class MainActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var mUserDao: UserDao
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
//        fakeData()
        initRecyclerView()
    }

    private fun initDatabaseManager() {
        myAppContext = this.applicationContext
        this.mUserDao = UserDao();
    }

    private fun initPaginatorUser() {
        val total: Long = this.mUserDao.countUser()
        Log.d("total", total.toString())
        mPaginator = AndroidPaginator(1, AppConfig.Pagination.PAGE_SIZE, total)
    }

    private fun initRefresh() {
        mSwipeRefreshLayoutMain = findViewById(R.id.swipeRefreshLayoutMain)
        mSwipeRefreshLayoutMain.setOnRefreshListener(this)
    }

    private fun initRecyclerView() {
        mUsers = ArrayList(this.mUserDao.getUserList(mPaginator.getOffset(), mPaginator.mPageSize))
        Log.d("Users", mUsers.size.toString())
        if (mPaginator.hasNextPage()) {
            mPaginator.mPage++
        }

        mUserAdapter = object : UserAdapter(mUsers as ArrayList<User?>) {

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
            val users = ArrayList(this.mUserDao.getUserList(mPaginator.getOffset(), mPaginator.mPageSize))
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
        val total: Long = this.mUserDao.countUser()
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
            this.mUserDao.insertUser(
                User(null, "Test ${total + i}", sex)
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