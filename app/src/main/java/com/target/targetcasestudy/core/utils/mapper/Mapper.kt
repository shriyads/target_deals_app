package com.target.targetcasestudy.core.utils.mapper



interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}
