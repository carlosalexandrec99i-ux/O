package com.postador.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class PostadorService : AccessibilityService() {

    companion object {
        var instance: PostadorService? = null
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onServiceConnected() {
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onUnbind(intent: Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }

    fun testarInstagram() {
        val intent = packageManager.getLaunchIntentForPackage("com.instagram.android")
        if (intent == null) {
            aviso("Instagram não encontrado")
            return
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        handler.postDelayed({
            val ok = clicarPorTexto(listOf("Criar", "Nova publicação", "Create", "New post"))
            aviso(if (ok) "Cliquei no +" else "Não achei o botão")
        }, 5000)
    }

    private fun clicarPorTexto(alvos: List<String>): Boolean {
        val raiz = rootInActiveWindow ?: return false
        val no = achar(raiz, alvos) ?: return false
        var atual: AccessibilityNodeInfo? = no
        while (atual != null) {
            if (atual.isClickable) {
                return atual.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
            atual = atual.parent
        }
        return false
    }

    private fun achar(no: AccessibilityNodeInfo, alvos: List<String>): AccessibilityNodeInfo? {
        val textos = listOfNotNull(no.contentDescription?.toString(), no.text?.toString())
        if (textos.any { t -> alvos.any { t.equals(it, ignoreCase = true) } }) return no
        for (i in 0 until no.childCount) {
            val filho = no.getChild(i) ?: continue
            val achado = achar(filho, alvos)
            if (achado != null) return achado
        }
        return null
    }

    private fun aviso(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
