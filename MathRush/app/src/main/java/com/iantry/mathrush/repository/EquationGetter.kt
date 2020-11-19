package com.iantry.mathrush.repository

import com.iantry.mathrush.Equation

interface EquationGetter {

    fun generateEquations()
    fun retrieveEquation(): Equation
}