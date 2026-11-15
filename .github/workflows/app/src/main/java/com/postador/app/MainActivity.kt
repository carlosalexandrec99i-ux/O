package com.postador.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }

        val titulo = TextView(this).apply {
            text = "Postador"
            textSize = 28f
        }

        val botaoAcessibilidade = Button(this).apply {
            text = "1. Ativar acessibilidade"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        val botaoTeste = Button(this).apply {
            text = "2. Testar: abrir Instagram e tocar no +"
            setOnClickListener {
                val servico = PostadorService.instance
                if (servico == null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Ative a acessibilidade primeiro",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    servico.testarInstagram()
                }
            }
        }

        layout.addView(titulo)
        layout.addView(botaoAcessibilidade)
        layout.addView(botaoTeste)
        setContentView(layout)
    }
}
