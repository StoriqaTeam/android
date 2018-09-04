package com.storiqa.market.model.reponse

import com.storiqa.market.util.log
import org.json.JSONObject

class AuthErrorPayload (payload: String) {
    private val emailProblem: String
    private val passProblem: String

    init {
        val pay = JSONObject(payload)

        var em = ""
        try {
            val emailArray = pay.getJSONArray("email")
            for(i in 0 until emailArray.length()) {
                val tt = emailArray[i] as JSONObject
                em += tt.getString("message")
            }
        } catch (e: Exception) {
            log("can't parse email msg")
        }
        emailProblem = em

        var pa = ""
        try {
            val passArray = pay.getJSONArray("password")
            for(i in 0 until passArray.length()) {
                val tt = passArray[i] as JSONObject
                pa += tt.getString("message")
            }
        } catch (ex: Exception) {
            log("can't parse password msg")
        }
        passProblem = pa
    }

    override fun toString(): String {
        return "\n $emailProblem \n $passProblem"
    }


}