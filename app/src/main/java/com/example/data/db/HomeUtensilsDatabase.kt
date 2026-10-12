package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.ActivationCycleDao
import com.example.data.dao.ActivationFundingTargetDao
import com.example.data.dao.AgentApplicationDao
import com.example.data.dao.AgentCommissionLogDao
import com.example.data.dao.AgentFacilitationDao
import com.example.data.dao.AgentWorkingFloatDao
import com.example.data.dao.BeneficiaryAuditLogDao
import com.example.data.dao.BeneficiaryDetailsDao
import com.example.data.dao.P2PTransferDao
import com.example.data.dao.ReferralDao
import com.example.data.dao.TaskSubmissionDao
import com.example.data.dao.UserProfileDao
import com.example.data.dao.UtensilTaskDao
import com.example.data.dao.WalletTransactionDao
import com.example.data.dao.WithdrawalMethodDao
import com.example.data.dao.WithdrawalRequestDao
import com.example.data.model.ActivationCycleEntity
import com.example.data.model.ActivationFundingTargetEntity
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.AgentCommissionLogEntity
import com.example.data.model.AgentFacilitationEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.data.model.BeneficiaryAuditLogEntity
import com.example.data.model.BeneficiaryDetailsEntity
import com.example.data.model.P2PTransferEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.TaskSubmissionEntity
import com.example.data.model.UserProfileEntity
import com.example.data.dao.AgentActivationRequestDao
import com.example.data.dao.AgentPaymentConfigDao
import com.example.data.dao.AgentPaymentProtectionCaseDao
import com.example.data.dao.AgentPhotoAuditDao
import com.example.data.dao.PaymentProofAccessLogDao
import com.example.data.dao.UgandaHolidayDao
import com.example.data.dao.UgandaHolidayAuditLogDao
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentPaymentConfigEntity
import com.example.data.model.AgentPaymentProtectionCaseEntity
import com.example.data.model.AgentPhotoAuditEntity
import com.example.data.model.PaymentProofAccessLogEntity
import com.example.data.model.UgandaHolidayEntity
import com.example.data.model.UgandaHolidayAuditLogEntity
import com.example.data.model.UtensilTaskEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity

@Database(
    entities = [
        UtensilTaskEntity::class,
        UserProfileEntity::class,
        TaskSubmissionEntity::class,
        WalletTransactionEntity::class,
        ReferralEntity::class,
        ActivationCycleEntity::class,
        P2PTransferEntity::class,
        ActivationFundingTargetEntity::class,
        AgentFacilitationEntity::class,
        AgentWorkingFloatEntity::class,
        WithdrawalRequestEntity::class,
        WithdrawalMethodEntity::class,
        BeneficiaryDetailsEntity::class,
        BeneficiaryAuditLogEntity::class,
        AgentApplicationEntity::class,
        AgentCommissionLogEntity::class,
        AgentActivationRequestEntity::class,
        AgentPaymentProtectionCaseEntity::class,
        AgentPaymentConfigEntity::class,
        AgentPhotoAuditEntity::class,
        PaymentProofAccessLogEntity::class,
        UgandaHolidayEntity::class,
        UgandaHolidayAuditLogEntity::class
    ],
    version = 11,
    exportSchema = false
)
abstract class HomeUtensilsDatabase : RoomDatabase() {
    abstract fun utensilTaskDao(): UtensilTaskDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun taskSubmissionDao(): TaskSubmissionDao
    abstract fun walletTransactionDao(): WalletTransactionDao
    abstract fun referralDao(): ReferralDao
    abstract fun activationCycleDao(): ActivationCycleDao
    abstract fun p2pTransferDao(): P2PTransferDao
    abstract fun activationFundingTargetDao(): ActivationFundingTargetDao
    abstract fun agentFacilitationDao(): AgentFacilitationDao
    abstract fun agentWorkingFloatDao(): AgentWorkingFloatDao
    abstract fun withdrawalRequestDao(): WithdrawalRequestDao
    abstract fun withdrawalMethodDao(): WithdrawalMethodDao
    abstract fun beneficiaryDetailsDao(): BeneficiaryDetailsDao
    abstract fun beneficiaryAuditLogDao(): BeneficiaryAuditLogDao
    abstract fun agentApplicationDao(): AgentApplicationDao
    abstract fun agentCommissionLogDao(): AgentCommissionLogDao
    abstract fun agentActivationRequestDao(): AgentActivationRequestDao
    abstract fun agentPaymentProtectionCaseDao(): AgentPaymentProtectionCaseDao
    abstract fun agentPaymentConfigDao(): AgentPaymentConfigDao
    abstract fun agentPhotoAuditDao(): AgentPhotoAuditDao
    abstract fun paymentProofAccessLogDao(): PaymentProofAccessLogDao
    abstract fun ugandaHolidayDao(): UgandaHolidayDao
    abstract fun ugandaHolidayAuditLogDao(): UgandaHolidayAuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: HomeUtensilsDatabase? = null

        fun getDatabase(context: Context): HomeUtensilsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HomeUtensilsDatabase::class.java,
                    "home_utensils_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
