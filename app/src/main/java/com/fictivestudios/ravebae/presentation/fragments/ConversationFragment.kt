package com.fictivestudios.ravebae.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fictivestudios.imdfitness.activities.fragments.BaseFragment
import com.fictivestudios.ravebae.R
import com.fictivestudios.ravebae.core.storage.ConfigurationPref
import com.fictivestudios.ravebae.presentation.adapter.ConversationAdapter
import com.fictivestudios.ravebae.presentation.models.Chat
import com.fictivestudios.ravebae.presentation.models.ChatItem
import com.fictivestudios.ravebae.presentation.models.ChatRecieveModel
import com.fictivestudios.ravebae.presentation.models.ChatSendModel
import com.fictivestudios.ravebae.utils.Titlebar
import com.gigo.clean.networksetup.socket.SocketApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Ack
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_conversation.view.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConversationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConversationFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var mView: View

    var socket: Socket? = null

    var messageList = ArrayList<ChatRecieveModel>()
    var recieverId: String? = null

    var userImageUrl: String? = ""
    var userName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Instead of sending from fragment, receiving from preference..
//        recieverId = this.requireArguments().getString("chatreceiverid")
//        userImageUrl = this.requireArguments().getString("chatsenderphotourl")
//        userName = this.requireArguments().getString("chatusername")
    }

    override fun setTitlebar(titlebar: Titlebar) {
//        recieverId = this.requireArguments().getString("chatreceiverid")
//        userImageUrl = this.requireArguments().getString("chatsenderphotourl")
//        userName = this.requireArguments().getString("chatusername")

        recieverId = ConfigurationPref(requireActivity()).chatReceiverId
        userImageUrl = ConfigurationPref(requireActivity()).chatSenderPhotoUrl
        // userImageUrl = ConfigurationPref(requireActivity()).profilePic
        userName = ConfigurationPref(requireActivity()).chatUserName

        // Has to set photo

        // ,userImageUrl.toString() // ConfigurationPref(requireActivity()).userId // ConfigurationPref(requireActivity()).chatSenderId
        titlebar.setProfileBtn(
            "" + userName,
            "" + userImageUrl,
            "conversation",
            "" + recieverId
        )
        titlebar.showTitleBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_conversation, container, false)

//        var chatList = ArrayList<Chat>()

//        chatList.add(Chat("Hi",Constants.VIEW_TYPE_MESSAGE_RECEIVED))
//        chatList.add(Chat("Hello",Constants.VIEW_TYPE_MESSAGE_SENT))
//        chatList.add(Chat("How are you..?",Constants.VIEW_TYPE_MESSAGE_RECEIVED))
//        chatList.add(Chat("Fine what about you..?",Constants.VIEW_TYPE_MESSAGE_SENT))

        // mView.rv_conversation.adapter = ConversationAdapter()

        getSocket()

        mView.btn_send.setOnClickListener {
            if (socket == null) {
                Toast.makeText(requireActivity(), "Socket Not Connected", Toast.LENGTH_SHORT).show()
            } else {
                // Send Message
                Log.d(
                    "btnsend",
                    "Sender(User) ID:" + ConfigurationPref(requireActivity()).userId + " Reciever ID:" + recieverId
                )
                // Testing for infinix test100 to test102
                val model = ChatSendModel(
                    ConfigurationPref(requireActivity()).userId,
                    recieverId,
                    "" + mView.et_write_message.text
                )
                // Clear edittext chat message
                mView.et_write_message.setText("")

                //        Log.e("GetMessage", "userData.toString() " + userData.id)
                //      Log.e("GetMessage", "navArgs.value.bookingId " + navArgs.value.bookingId)
                var jsonObject: JSONObject? = null
                val gson: Gson? = Gson()
                if (gson != null) {
                    jsonObject = JSONObject(gson.toJson(model))
                }
                if (socket != null && socket!!.connected()) {

                    // binding?.etWrite?.setText("")

                    socket!!.emit("send_message", jsonObject, Ack() {
                    });
                    Log.e("send_message", "jsonObject" + jsonObject.toString())
                } else {
                    Toast.makeText(
                        activity,
                        "Not Connect ,Please reload this screen",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Initializing Adapter // Testing
        mView.rv_conversation.adapter = ConversationAdapter(messageList, requireActivity())

        return mView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConversationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConversationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getSocket() {

        if (socket == null) {
            val app: SocketApp = SocketApp.instance!!
            socket = app.socket
            socket!!.on(Socket.EVENT_CONNECT, onConnect)
            socket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            socket!!.on("response", response)
            socket!!.on("error", error)
            socket!!.connect()
        }

    }

    private val onConnect = Emitter.Listener {
        Handler(Looper.getMainLooper()).post {
            var toast: Toast? = null
            Log.e("Socket-onConnect", "Socket-onConnect")
            // //   toast = Toast.makeText(this, "Connected", Toast.LENGTH_SHORT)
            //    toast.show()
            try {
                // getMessageListener2.onGetMessage()
                getMessage()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

        }
    }
    private val onConnectError = Emitter.Listener {
        Handler(Looper.getMainLooper()).post {
            Log.e("Socket-onConnectError", "Socket-onConnectError")
            var toast: Toast? = null
            //   toast = Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT)
            //   toast.show()
        }
    }
    private val onDisconnect = Emitter.Listener {
        Handler(Looper.getMainLooper()).post {
            var toast: Toast? = null
            //  Log.e("Socket-Disconnected", "Socket-Disconnected")
            //  toast = Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT)
            //   toast.show()
        }
    }

    private fun getMessage() {
        // currentUserId, receiverUserId
        val parameterName =
            ChatItem(ConfigurationPref(requireActivity()).userId, recieverId)
//        Log.e("GetMessage", "userData.toString() " + userData.id)
        //      Log.e("GetMessage", "navArgs.value.bookingId " + navArgs.value.bookingId)
        var jsonObject: JSONObject? = null
        val gson: Gson? = Gson()
        if (gson != null) {
            jsonObject = JSONObject(gson.toJson(parameterName))
        }
        if (socket != null && socket!!.connected()) {
            socket!!.emit("get_messages", jsonObject, Ack() {
            });
            Log.e("get_messages", "jsonObject" + jsonObject.toString())
            // Here we will get messages list
        } else {
            Toast.makeText(activity, "Not Connect ,Please reload this screen", Toast.LENGTH_LONG)
                .show()
        }

    }

    private val receiveMessage = Emitter.Listener { arg ->
        Handler(Looper.getMainLooper()).post {
            val data = arg[0] as JSONObject
            Log.e("receiveMessage", "" + data.getString("data"))
            val gson = Gson()
            val listFromGson: ArrayList<ChatItem> = gson.fromJson(
                data.getString("data"),
                object : TypeToken<ArrayList<ChatItem?>?>() {}.type
            )
//            if (listFromGson.size != 1) {
//                messageList = ArrayList()
//                messageList = listFromGson
//                if (!messageList.isNullOrEmpty())
//                {
//                    chatAdapter = context?.let { ConversationAdapter(it, messageList!!,currentUserId) }
//                    binding?.chatList?.adapter = chatAdapter
//                    binding?.chatList?.adapter?.notifyDataSetChanged()
//                }
//
//                Log.e("receiveMessage", listFromGson.toString())
//            }
//            else{
//                messageList?.add(listFromGson.get(0))
//                binding?.chatList!!.scrollToPosition(messageList!!.size - 1);
//                chatAdapter?.notifyItemChanged(messageList!!.size - 1)
//            }

        }
    }

    fun offSocket() {
        try {
            if (socket != null && socket!!.connected()) {
                socket!!.off(Socket.EVENT_CONNECT, onConnect)
                socket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
                socket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
                socket!!.off("send_message", response)
                socket!!.off("get_messages", error)
                socket!!.disconnect()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Message will recieve here..
    private val response = Emitter.Listener { arg ->

        Handler(Looper.getMainLooper()).post {
            val data = arg[0] as JSONObject
            Log.e("SendMessage2", "" + data.getString("data"))

            try {
                val gson = Gson()
                val listFromGson: ArrayList<ChatRecieveModel> = gson.fromJson(
                    data.getString("data"),
                    object : TypeToken<ArrayList<ChatRecieveModel?>?>() {}.type
                )
//                // Testing
//                if (listFromGson.size > 0) {
//                    messageList = listFromGson
//                    if (!messageList.isNullOrEmpty()) {
//                        mView.rv_conversation.adapter =
//                            ConversationAdapter(messageList, requireActivity())
//
//                        mView.rv_conversation!!.scrollToPosition(messageList.size - 1);
//
//                        mView.rv_conversation?.adapter?.notifyDataSetChanged()
//                    }
//                    Log.e("receiveMessage", listFromGson.toString())
//                }


                if (listFromGson.size != 1) {
                    messageList = listFromGson
                    if (!messageList.isNullOrEmpty())
                    {
                        mView.rv_conversation.adapter = ConversationAdapter(messageList, requireActivity())
                        mView.rv_conversation?.adapter?.notifyDataSetChanged()

                        // set photo
                        // senderImageURL


                        //chatAdapter = context?.let { ConversationAdapter(it, messageList!!,currentUserId) }
                        //binding?.chatList?.adapter = chatAdapter
                        // binding?.chatList?.adapter?.notifyDataSetChanged()
                    }

                    Log.e("receiveMessage", listFromGson.toString())
                }
                else{
                    messageList?.add(listFromGson.get(0))
//                    mView.rv_conversation.adapter?.notifyItemChanged(messageList!!.size - 1)
//                    mView.rv_conversation!!.scrollToPosition(messageList!!.size - 1);
                    // Changing order test
                    mView.rv_conversation!!.scrollToPosition(messageList!!.size - 1);
                    mView.rv_conversation.adapter?.notifyItemChanged(messageList!!.size - 1)



                    // binding?.chatList!!.scrollToPosition(messageList!!.size - 1);
                    // chatAdapter?.notifyItemChanged(messageList!!.size - 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

//            val gson = Gson()
//            val listFromGson: ArrayList<ChatRecieveModel> = gson.fromJson(
//                data.getString("data"),
//                object : TypeToken<ArrayList<ChatRecieveModel?>?>() {}.type
//            )
//            if (listFromGson.size != 1) {
//                messageList = listFromGson
//                if (!messageList.isNullOrEmpty())
//                {
//                    mView.rv_conversation.adapter = ConversationAdapter(messageList, requireActivity())
//                    mView.rv_conversation?.adapter?.notifyDataSetChanged()
//
//                    //chatAdapter = context?.let { ConversationAdapter(it, messageList!!,currentUserId) }
//                    //binding?.chatList?.adapter = chatAdapter
//                    // binding?.chatList?.adapter?.notifyDataSetChanged()
//                }
//
//                Log.e("receiveMessage", listFromGson.toString())
//            }
//            else{
//                messageList?.add(listFromGson.get(0))
//                mView.rv_conversation.adapter?.notifyItemChanged(messageList!!.size - 1)
//                mView.rv_conversation!!.scrollToPosition(messageList!!.size - 1);
//
//                 // binding?.chatList!!.scrollToPosition(messageList!!.size - 1);
//                // chatAdapter?.notifyItemChanged(messageList!!.size - 1)
//            }


        }
    }


    private val error = Emitter.Listener { arg ->
        Log.e("ErrorListener", "error listener")

        Handler(Looper.getMainLooper()).post {
            val data = arg[0] as JSONObject
            // Log.e("GetMessage", "" + data.getString("data"))

        }
    }
}