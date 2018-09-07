package com.storiqa.market.model.reponse

import com.storiqa.market.util.log
import org.json.JSONObject

class AuthErrorPayload (payload: String) {
    val emailProblem: String?
    val passProblem: String?

    init {
        val pay = JSONObject(payload)

        var em: String? = ""
        try {
            val emailArray = pay.getJSONArray("email")
            for(i in 0 until emailArray.length()) {
                val tt = emailArray[i] as JSONObject
                em += tt.getString("message")
            }
        } catch (e: Exception) {
            log("can't parse email msg")
            em = null
        }
        emailProblem = em

        var pa: String? = ""
        try {
            val passArray = pay.getJSONArray("password")
            for(i in 0 until passArray.length()) {
                val tt = passArray[i] as JSONObject
                pa += tt.getString("message")
            }
        } catch (ex: Exception) {
            log("can't parse password msg")
            pa = null
        }
        passProblem = pa
    }

    override fun toString(): String {
        return "\n $emailProblem \n $passProblem"
    }


}