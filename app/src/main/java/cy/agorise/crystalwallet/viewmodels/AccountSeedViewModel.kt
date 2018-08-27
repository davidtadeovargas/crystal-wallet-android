package cy.agorise.crystalwallet.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import cy.agorise.crystalwallet.dao.CrystalDatabase
import cy.agorise.crystalwallet.models.AccountSeed




class AccountSeedViewModel : AndroidViewModel {

    private var accountSeed: LiveData<AccountSeed>? = null
    private val db: CrystalDatabase
    private val app: Application




    constructor(application: Application,db:CrystalDatabase): super(application) {
        this.app = application;
        this.db = db;
    }


    fun loadSeed(seedId: Long) {
        this.accountSeed = this.db.accountSeedDao().findByIdLiveData(seedId)
    }

    fun addSeed(seed: AccountSeed) {
        val newId = this.db.accountSeedDao().insertAccountSeed(seed)
        seed.id = newId
    }

    fun getAccountSeed(): LiveData<AccountSeed>? {
        return this.accountSeed
    }
}