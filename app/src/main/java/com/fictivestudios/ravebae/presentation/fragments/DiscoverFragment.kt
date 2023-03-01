package com.fictivestudios.ravebae.presentation.fragments

// import com.fictivestudios.ravebae.utils.ErrorDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.activites.MainActivity
import com.fictivestudios.ravebae.presentation.activites.ResgistrationActivity
import com.fictivestudios.ravebae.presentation.adapter.CardContainerAdapter
import com.fictivestudios.ravebae.presentation.models.Datam
import com.fictivestudios.ravebae.presentation.viewmodels.DiscoverViewModel
import com.fictivestudios.ravebae.utils.CustomProgressDialogue
import com.fictivestudios.ravebae.utils.GenericViewModelFactory
import com.fictivestudios.ravebae.utils.MyUtils
import com.fictivestudios.ravebae.utils.Titlebar
import com.google.gson.Gson
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.fragment_discover.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : BBaseFragment<DiscoverViewModel>(), CardStackListener {

    private lateinit var layoutManager: CardStackLayoutManager

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mView: View
    private var adapter: CardContainerAdapter? = null

    private var from: String? = null

    private var arr : ArrayList<Datam>? = null
    var arr_size=0;
    var current_size=0

    // private lateinit var layoutManager: CardStackLayoutManager

    override fun createViewModel(): DiscoverViewModel {
        val factory = GenericViewModelFactory(DiscoverViewModel(requireActivity()))
        return ViewModelProvider(this, factory).get(DiscoverViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Taking from value from pref..
        from = ConfigurationPref(requireActivity()).from

        from?.let{
            if(from.equals("SignUp",ignoreCase = true)){
//                MainActivity.getMainActivity
//                    ?.navController?.navigate(R.id.action_discoverFragment2_to_preferencesFragment)
//                MainActivity.getMainActivity?.supportFragmentManager?.beginTransaction()?.add(R.id.container, PreferencesFragment())?.commit()
                // MainActivity.getMainActivity?.supportFragmentManager?.beginTransaction()?.add(R.id.container, MyProfileFragment())?.commit()
            }
        }
    }

    override fun setTitlebar(titlebar: Titlebar) {

        titlebar.setHomeTitle(getString(R.string.discover))
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_discover, container, false)

//        MainActivity.getMainActivity?.bottomBar?.setActiveItem(1)

        layoutManager = CardStackLayoutManager(requireContext(), this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
            setStackFrom(StackFrom.Bottom)
            // By default horizontal changing to freedom
            setDirections(Direction.FREEDOM)
        }

        /*   var mList = ArrayList<String>()

           mList.add("1")
           mList.add("1")
           mList.add("1")
           mList.add("1")
           mList.add("1")
            val adapter = context?.let { com.fictivestudios.ravebae.presentation.adapter.CardContainerAdapter(mList, it) }
   */
//        val adapter = CardContainerAdapter()
//        mView.stack_view.setAdapter(adapter)

        callApi()

        mView.stack_view.layoutManager = layoutManager
        mView.stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

        return mView
    }

    private fun callApi() {
        try {
            viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadDiscoverData()
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadDiscoverData()
            viewModel?.DiscoverModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1) {
                        // Save model before passing
                        arr = homeMainModel.data
                        current_size=0
                        arr_size=homeMainModel.data.size
                        adapter = CardContainerAdapter(homeMainModel.data, requireActivity())
                        mView.stack_view.setAdapter(adapter)
                        adapter!!.notifyDataSetChanged()
                        onCardContainnerClick()
                    } else {
                         if (homeMainModel?.message=="Unauthorized"){
                              ConfigurationPref(requireActivity()).clear()
                              startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                              MainActivity.getMainActivity?.finish()
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                          }
                          else{
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                                  .show()
                          }

                        // Donothing,  error toast in viewmodel

                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("MatchList", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("MatchList", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("MatchList", "Catch Block")
            Toast.makeText(requireActivity(), "Invalid Credentials,please try again!", Toast.LENGTH_LONG).show()
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
        }
    }

    private fun callAddFriendApi(receiverID: String?, requestStatus: Int?, isHeart: Int?) {
        try {
            Log.d("AddFriend", "Receiver ID:" + receiverID)
            // viewModel?.loadingStatus?.observe(viewLifecycleOwner, LoadingObserver())

            if(MyUtils.isNetworkAvailable(requireActivity())){
                viewModel?.loadAddFriendData(receiverID, requestStatus, isHeart)
            } else{
                Toast.makeText(requireActivity(),getString(R.string.no_internet),Toast.LENGTH_LONG).show()
            }

//            viewModel?.loadAddFriendData(receiverID, requestStatus)
            viewModel?.addFriendModelData?.observe(viewLifecycleOwner) { homeMainModel ->
                try {
                    if (homeMainModel?.status == 1) {
                        Toast.makeText(
                            requireActivity(),
                            "" + homeMainModel.message,
                            Toast.LENGTH_LONG
                        ).show()
                         // Hit API and match list
                         callApi()
                    } else {
                         if (homeMainModel?.message=="Unauthorized"){
                              ConfigurationPref(requireActivity()).clear()
                              startActivity(Intent(requireContext(), ResgistrationActivity::class.java))
                              MainActivity.getMainActivity?.finish()
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG).show()
                          }
                          else{
                              Toast.makeText(requireActivity(), homeMainModel?.message, Toast.LENGTH_LONG)
                                  .show()
                          }

                        // Donothing,  error toast in viewmodel
                        // ErrorDialog.showErrorDialog(requireActivity(), homeMainModel?.message)
                        Log.d("AddFriend", "Else Block")
                    }
                } catch (e: Exception) {
                    Log.d("AddFriend", "Catch Block")
                }
            }
        } catch (ex: Exception) {
            Log.d("AddFriend", "Catch Block" + ex.printStackTrace())
            // ErrorDialog.showErrorDialog(requireActivity(), "Invalid Credentials,please try again!")
            Toast.makeText(
                requireActivity(),
                "Invalid Credentials,please try again!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //Observer
    private inner class LoadingObserver : Observer<Boolean?> {
        override fun onChanged(isLoading: Boolean?) {
            if (isLoading == null) return
            if (progress == null) progress = CustomProgressDialogue(activity)
            if (isLoading) {
                if (progress?.isShowing == false) progress?.show()
            } else {
                if (progress?.isShowing == true) progress?.dismiss()
            }
        }
    }

    /** Interface click listener */
    private fun onCardContainnerClick() {
        adapter?.setOnButtonClickListener(object :
            CardContainerAdapter.OnButtonClickListener {
            override fun onButtonCLick(
                position: Int,
                model: Datam?,
                action: String?
            ) {
                // Hit API for add friend
                // callAddFriendApi(model?.userId?.Id)

                if (action.equals("profile", ignoreCase = true)) {
                    // MainActivity.navController.navigate(R.id.viewPagerFragment)
                    // bundle.putString("fl", "1")
                    //navHostFragment!!.arguments=bundle
                    val gson = Gson()
                    val json: String = gson.toJson(model)

                    val bundle = Bundle()
                    bundle.putString("userprofile", "" + json)
                    bundle.putString("from", "discover")
                    bundle.putString("userId", model!!.userId!!.Id)

                    // bundle.putSerializable("",model)
                    MainActivity.getMainActivity
                        ?.navController?.navigate(R.id.userProfileFragment, bundle)

                } else if (action.equals("AddFriend", ignoreCase = true)) {
                    // Hit API for add friend // 0 status default add friend // status logic shifted to isHeart
                    callAddFriendApi(model?.userId?.Id, 0, 0)
                }
                else if(action.equals("AddHeart", ignoreCase = true)){
                   // Hit API for add friend with additional param // 3 status add friend by heart // status logic shifted to isHeart
                    callAddFriendApi(model?.userId?.Id, 0, 1)
                }
                else {
                    // delete case or remove case
                }

//                Toast.makeText(
//                    requireActivity(),
//                    "POSITION:" + position + " USERID:" + model?.userId?.Id,
//                    Toast.LENGTH_LONG
//                ).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiscoverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiscoverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    var ppostion = 0

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        // Add friend request..
        if(direction == Direction.Right){
            callAddFriendApi(arr?.get(ppostion)?.userId?.Id, 0, 0)
        }

        // Add friend request heart case..
        if(direction == Direction.Top){
//            arr?.get(ppostion)?.userId?.Id // 3 incase of heart // status logic shifted to isHeart
            callAddFriendApi(arr?.get(ppostion)?.userId?.Id,0, 1)
        }

        current_size=current_size+1
        Log.e("sfdnsjdf",""+current_size)
        if(arr_size==current_size){
            callApi()
        }
    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
        ppostion = position
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }


/*
    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
           }

    override fun onCardRewound() {
          }

    override fun onCardCanceled() {
           }

    override fun onCardAppeared(view: View?, position: Int) {
          }

    override fun onCardDisappeared(view: View?, position: Int) {
           }
*/


}