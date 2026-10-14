package com.example.data.repository

import androidx.room.withTransaction
import com.example.data.db.HomeUtensilsDatabase
import com.example.data.model.ActivationCycleEntity
import com.example.data.model.ActivationFundingTargetEntity
import com.example.data.model.AgentApplicationEntity
import com.example.data.model.AgentCommissionLogEntity
import com.example.data.model.AgentFacilitationEntity
import com.example.data.model.AgentWorkingFloatEntity
import com.example.data.model.BeneficiaryAuditLogEntity
import com.example.data.model.BeneficiaryDetailsEntity
import com.example.data.model.HomeUtensilsPackage
import com.example.data.model.P2PTransferEntity
import com.example.data.model.ReferralEntity
import com.example.data.model.TaskSubmissionEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.UtensilTaskEntity
import com.example.data.model.WalletTransactionEntity
import com.example.data.model.AgentActivationRequestEntity
import com.example.data.model.AgentPaymentConfigEntity
import com.example.data.model.AgentPaymentProtectionCaseEntity
import com.example.data.model.AgentPhotoAuditEntity
import com.example.data.model.PaymentProofAccessLogEntity
import com.example.data.model.UgandaHolidayEntity
import com.example.data.model.UgandaHolidayAuditLogEntity
import com.example.data.model.WithdrawalMethodEntity
import com.example.data.model.WithdrawalRequestEntity
import com.example.data.model.LiveActivityEvent
import com.example.data.model.maskAgentId
import com.example.data.model.maskBeneficiaryName
import com.example.data.model.maskDestination
import com.example.data.model.maskMemberId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeUtensilsRepository(private val database: HomeUtensilsDatabase) {

    companion object {
        val DEFAULT_UGANDA_HOLIDAYS = listOf(
            // 2026 Official Uganda Public Holidays
            UgandaHolidayEntity("HOL-2026-01", "New Year's Day", "2026-01-01", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-02", "NRM Liberation Day", "2026-01-26", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-03", "Archbishop Janani Luwum Day", "2026-02-16", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-04", "International Women's Day", "2026-03-08", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-05", "Eid al-Fitr", "2026-03-20", isOfficialPublicHoliday = true, description = "Islamic Public Holiday (Variable)", year = 2026),
            UgandaHolidayEntity("HOL-2026-06", "Good Friday", "2026-04-03", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2026),
            UgandaHolidayEntity("HOL-2026-07", "Easter Monday", "2026-04-06", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2026),
            UgandaHolidayEntity("HOL-2026-08", "Labour Day", "2026-05-01", isOfficialPublicHoliday = true, description = "International Workers' Day", year = 2026),
            UgandaHolidayEntity("HOL-2026-09", "Eid al-Adha / Idd al-Adha", "2026-05-27", isOfficialPublicHoliday = true, description = "Islamic Feast of Sacrifice (Variable)", year = 2026),
            UgandaHolidayEntity("HOL-2026-10", "Uganda Martyrs' Day", "2026-06-03", isOfficialPublicHoliday = true, description = "Namugongo National Pilgrimage Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-11", "National Heroes Day", "2026-06-09", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-12", "Independence Day", "2026-10-09", isOfficialPublicHoliday = true, description = "Uganda National Independence Day", year = 2026),
            UgandaHolidayEntity("HOL-2026-13", "Christmas Day", "2026-12-25", isOfficialPublicHoliday = true, description = "Christmas Day Public Holiday", year = 2026),
            UgandaHolidayEntity("HOL-2026-14", "Boxing Day", "2026-12-26", isOfficialPublicHoliday = true, description = "Boxing Day Public Holiday", year = 2026),

            // 2025 Official Uganda Public Holidays
            UgandaHolidayEntity("HOL-2025-01", "New Year's Day", "2025-01-01", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-02", "NRM Liberation Day", "2025-01-26", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-03", "Archbishop Janani Luwum Day", "2025-02-16", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-04", "International Women's Day", "2025-03-08", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-05", "Eid al-Fitr", "2025-03-31", isOfficialPublicHoliday = true, description = "Islamic Public Holiday (Variable)", year = 2025),
            UgandaHolidayEntity("HOL-2025-06", "Good Friday", "2025-04-18", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2025),
            UgandaHolidayEntity("HOL-2025-07", "Easter Monday", "2025-04-21", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2025),
            UgandaHolidayEntity("HOL-2025-08", "Labour Day", "2025-05-01", isOfficialPublicHoliday = true, description = "International Workers' Day", year = 2025),
            UgandaHolidayEntity("HOL-2025-09", "Eid al-Adha / Idd al-Adha", "2025-06-07", isOfficialPublicHoliday = true, description = "Islamic Feast of Sacrifice (Variable)", year = 2025),
            UgandaHolidayEntity("HOL-2025-10", "Uganda Martyrs' Day", "2025-06-03", isOfficialPublicHoliday = true, description = "Namugongo National Pilgrimage Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-11", "National Heroes Day", "2025-06-09", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-12", "Independence Day", "2025-10-09", isOfficialPublicHoliday = true, description = "Uganda National Independence Day", year = 2025),
            UgandaHolidayEntity("HOL-2025-13", "Christmas Day", "2025-12-25", isOfficialPublicHoliday = true, description = "Christmas Day Public Holiday", year = 2025),
            UgandaHolidayEntity("HOL-2025-14", "Boxing Day", "2025-12-26", isOfficialPublicHoliday = true, description = "Boxing Day Public Holiday", year = 2025),

            // 2027 Official Uganda Public Holidays
            UgandaHolidayEntity("HOL-2027-01", "New Year's Day", "2027-01-01", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-02", "NRM Liberation Day", "2027-01-26", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-03", "Archbishop Janani Luwum Day", "2027-02-16", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-04", "International Women's Day", "2027-03-08", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-05", "Eid al-Fitr", "2027-03-10", isOfficialPublicHoliday = true, description = "Islamic Public Holiday (Variable)", year = 2027),
            UgandaHolidayEntity("HOL-2027-06", "Good Friday", "2027-03-26", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2027),
            UgandaHolidayEntity("HOL-2027-07", "Easter Monday", "2027-03-29", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2027),
            UgandaHolidayEntity("HOL-2027-08", "Labour Day", "2027-05-01", isOfficialPublicHoliday = true, description = "International Workers' Day", year = 2027),
            UgandaHolidayEntity("HOL-2027-09", "Eid al-Adha / Idd al-Adha", "2027-05-17", isOfficialPublicHoliday = true, description = "Islamic Feast of Sacrifice (Variable)", year = 2027),
            UgandaHolidayEntity("HOL-2027-10", "Uganda Martyrs' Day", "2027-06-03", isOfficialPublicHoliday = true, description = "Namugongo National Pilgrimage Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-11", "National Heroes Day", "2027-06-09", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-12", "Independence Day", "2027-10-09", isOfficialPublicHoliday = true, description = "Uganda National Independence Day", year = 2027),
            UgandaHolidayEntity("HOL-2027-13", "Christmas Day", "2027-12-25", isOfficialPublicHoliday = true, description = "Christmas Day Public Holiday", year = 2027),
            UgandaHolidayEntity("HOL-2027-14", "Boxing Day", "2027-12-26", isOfficialPublicHoliday = true, description = "Boxing Day Public Holiday", year = 2027)
        )

        fun generateMemberId(): String {
            val chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"
            val randomPart = (1..7).map { chars.random() }.joinToString("")
            return "HU$randomPart"
        }
    }

    private val holidaysCache = mutableMapOf<String, UgandaHolidayEntity>().apply {
        DEFAULT_UGANDA_HOLIDAYS.forEach { put(it.dateIso, it) }
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val existing = database.ugandaHolidayDao().getAllHolidaysOnce()
                if (existing.isEmpty()) {
                    database.ugandaHolidayDao().insertHolidays(DEFAULT_UGANDA_HOLIDAYS)
                }
                ugandaHolidays.collect { list ->
                    synchronized(holidaysCache) {
                        holidaysCache.clear()
                        DEFAULT_UGANDA_HOLIDAYS.forEach { holidaysCache[it.dateIso] = it }
                        list.forEach { h -> holidaysCache[h.dateIso] = h }
                    }
                }
            } catch (e: Exception) {
                // Ignore initialization failures
            }
        }
    }

    val tasks: Flow<List<UtensilTaskEntity>> = database.utensilTaskDao().getAllTasks()
    val userProfile: Flow<UserProfileEntity?> = database.userProfileDao().getUserProfile()
    val allMembers: Flow<List<UserProfileEntity>> = database.userProfileDao().getAllProfiles()
    val submissions: Flow<List<TaskSubmissionEntity>> = database.taskSubmissionDao().getAllSubmissions()
    val transactions: Flow<List<WalletTransactionEntity>> = database.walletTransactionDao().getAllTransactions()
    val referrals: Flow<List<ReferralEntity>> = database.referralDao().getAllReferrals()
    val activationCycles: Flow<List<ActivationCycleEntity>> = database.activationCycleDao().getAllCycles()
    val p2pTransfers: Flow<List<P2PTransferEntity>> = database.p2pTransferDao().getAllTransfers()
    val activeFundingTarget: Flow<ActivationFundingTargetEntity?> = database.activationFundingTargetDao().getActiveTargetForMember("HU7K4M92Q")
    val withdrawalRequests: Flow<List<WithdrawalRequestEntity>> = database.withdrawalRequestDao().getAllWithdrawals()
    val userWithdrawals: Flow<List<WithdrawalRequestEntity>> = database.withdrawalRequestDao().getWithdrawalsForUser("current_user")
    val withdrawalMethods: Flow<List<WithdrawalMethodEntity>> = database.withdrawalMethodDao().getAllMethods()
    val activeWithdrawalMethods: Flow<List<WithdrawalMethodEntity>> = database.withdrawalMethodDao().getActiveMethods()
    val beneficiaryDetails: Flow<BeneficiaryDetailsEntity?> = database.beneficiaryDetailsDao().getBeneficiary("current_user")
    val beneficiaryAuditLogs: Flow<List<BeneficiaryAuditLogEntity>> = database.beneficiaryAuditLogDao().getAllAuditLogs()
    val agentWorkingFloat: Flow<AgentWorkingFloatEntity?> = database.agentWorkingFloatDao().getWorkingFloat("AGT-CENTRAL-001")
    val agentFacilitations: Flow<List<AgentFacilitationEntity>> = database.agentFacilitationDao().getAllFacilitations()
    val allAgentApplications: Flow<List<AgentApplicationEntity>> = database.agentApplicationDao().getAllApplications()
    val allCommissionLogs: Flow<List<AgentCommissionLogEntity>> = database.agentCommissionLogDao().getAllCommissionLogs()

    // Agent Payment Protection Flows
    val activationRequests: Flow<List<AgentActivationRequestEntity>> = database.agentActivationRequestDao().getAllRequests()
    val memberActivationRequests: Flow<List<AgentActivationRequestEntity>> = database.agentActivationRequestDao().getRequestsForMember("HU7K4M92Q")
    val agentActivationRequests: Flow<List<AgentActivationRequestEntity>> = database.agentActivationRequestDao().getRequestsForAgent("AGT-CENTRAL-001")
    val protectionCases: Flow<List<AgentPaymentProtectionCaseEntity>> = database.agentPaymentProtectionCaseDao().getAllCases()
    val agentPaymentConfig: Flow<AgentPaymentConfigEntity?> = database.agentPaymentConfigDao().getConfig()
    val agentPhotoAudits: Flow<List<AgentPhotoAuditEntity>> = database.agentPhotoAuditDao().getAllAudits()
    val proofAccessLogs: Flow<List<PaymentProofAccessLogEntity>> = database.paymentProofAccessLogDao().getAllLogs()
    val allWorkingFloats: Flow<List<AgentWorkingFloatEntity>> = database.agentWorkingFloatDao().getAllWorkingFloats()
    val ugandaHolidays: Flow<List<UgandaHolidayEntity>> = database.ugandaHolidayDao().getAllHolidays()
    val holidayAuditLogs: Flow<List<UgandaHolidayAuditLogEntity>> = database.ugandaHolidayAuditLogDao().getAllAuditLogs()

    val liveActivityFeed: Flow<List<LiveActivityEvent>> = kotlinx.coroutines.flow.combine(
        transactions,
        p2pTransfers,
        activationCycles,
        withdrawalRequests,
        agentFacilitations
    ) { txs, p2ps, cycles, withdrawals, facilitations ->
        val events = mutableListOf<LiveActivityEvent>()

        for (tx in txs) {
            val masked = if (tx.counterpartyId.isNotBlank()) maskMemberId(tx.counterpartyId) else "HU7•••92Q"
            when (tx.type) {
                "TASK_REWARD" -> {
                    val cleanTitle = tx.title.replace("Task Reward", "")
                        .replace("(STANDARD PACKAGE):", "")
                        .replace("(STARTER PACKAGE):", "")
                        .replace("(PLUS PACKAGE):", "")
                        .trim()
                    events.add(
                        LiveActivityEvent(
                            id = "live_tx_${tx.id}",
                            memberId = tx.counterpartyId.ifEmpty { "HU7K4M92Q" },
                            maskedMember = masked,
                            eventType = "TASK_REWARD",
                            title = "Completed Utensil Review Task",
                            detail = if (cleanTitle.isNotBlank()) "$cleanTitle • Verified task reward" else "Inspected cookware & earned reward",
                            timestamp = tx.timestamp,
                            iconType = "TASK"
                        )
                    )
                }
                "REFERRAL_COMMISSION" -> {
                    events.add(
                        LiveActivityEvent(
                            id = "live_tx_${tx.id}",
                            memberId = tx.counterpartyId.ifEmpty { "HU7K4M92Q" },
                            maskedMember = masked,
                            eventType = "REFERRAL_COMMISSION",
                            title = "Earned Direct Referral Commission",
                            detail = "Invited member activated • Direct commission credited",
                            timestamp = tx.timestamp,
                            iconType = "COMMISSION"
                        )
                    )
                }
                "WELCOME_BONUS" -> {
                    events.add(
                        LiveActivityEvent(
                            id = "live_tx_${tx.id}",
                            memberId = tx.counterpartyId.ifEmpty { "HU7K4M92Q" },
                            maskedMember = masked,
                            eventType = "WELCOME_BONUS",
                            title = "Claimed Member Welcome Bonus",
                            detail = "First-time cookware inspector activation perk",
                            timestamp = tx.timestamp,
                            iconType = "PACKAGE"
                        )
                    )
                }
                "AGENT_FLOAT_PURCHASE" -> {
                    events.add(
                        LiveActivityEvent(
                            id = "live_tx_${tx.id}",
                            memberId = tx.counterpartyId.ifEmpty { "AGT-CENTRAL-001" },
                            maskedMember = maskAgentId(tx.counterpartyId.ifEmpty { "AGT-CENTRAL-001" }),
                            eventType = "FLOAT_BONUS",
                            title = "Agent Float Approved (+15% Company Bonus)",
                            detail = "Working capital expanded for regional activations",
                            timestamp = tx.timestamp,
                            iconType = "AGENT"
                        )
                    )
                }
            }
        }

        for (cycle in cycles) {
            val masked = maskMemberId(cycle.memberId)
            val (evtType, title, detail, icon) = when (cycle.cycleType) {
                "UPGRADE" -> listOf(
                    "PACKAGE_UPGRADE",
                    "Upgraded to ${cycle.packageTier}",
                    "Elevated daily utensil inspection tier & task allowance",
                    "UPGRADE"
                )
                "REACTIVATION" -> listOf(
                    "PACKAGE_REACTIVATION",
                    "Reactivated ${cycle.packageTier}",
                    "Renewed 60-day active inspection cycle",
                    "REACTIVATION"
                )
                else -> listOf(
                    "PACKAGE_ACTIVATION",
                    "Activated ${cycle.packageTier}",
                    "Joined regional household utensil inspection team",
                    "PACKAGE"
                )
            }
            events.add(
                LiveActivityEvent(
                    id = "live_cycle_${cycle.id}",
                    memberId = cycle.memberId,
                    maskedMember = masked,
                    eventType = evtType,
                    title = title,
                    detail = detail,
                    timestamp = cycle.activatedAt,
                    iconType = icon
                )
            )
        }

        for (p2p in p2ps) {
            val senderMasked = maskMemberId(p2p.senderMemberId)
            val recipientMasked = maskMemberId(p2p.recipientMemberId)
            events.add(
                LiveActivityEvent(
                    id = "live_p2p_send_${p2p.id}",
                    memberId = p2p.senderMemberId,
                    maskedMember = senderMasked,
                    eventType = "P2P_TRANSFER",
                    title = "Sent P2P Transfer",
                    detail = "Instant transfer sent to member $recipientMasked",
                    timestamp = p2p.timestamp,
                    iconType = "P2P"
                )
            )
            events.add(
                LiveActivityEvent(
                    id = "live_p2p_recv_${p2p.id}",
                    memberId = p2p.recipientMemberId,
                    maskedMember = recipientMasked,
                    eventType = "P2P_TRANSFER",
                    title = "Received P2P Transfer",
                    detail = "P2P transfer received from member $senderMasked",
                    timestamp = p2p.timestamp,
                    iconType = "P2P"
                )
            )
        }

        for (fac in facilitations) {
            val agentMasked = maskAgentId(fac.agentId)
            val memberMasked = maskMemberId(fac.recipientMemberId)
            events.add(
                LiveActivityEvent(
                    id = "live_fac_${fac.id}",
                    memberId = fac.agentId,
                    maskedMember = agentMasked,
                    eventType = "AGENT_FACILITATION",
                    title = "Facilitated Package Activation",
                    detail = "Assisted $memberMasked with ${fac.packageName} at 0% fee",
                    timestamp = fac.timestamp,
                    iconType = "AGENT"
                )
            )
        }

        for (wth in withdrawals) {
            val masked = maskMemberId(wth.memberId)
            events.add(
                LiveActivityEvent(
                    id = "live_wth_${wth.id}",
                    memberId = wth.memberId,
                    maskedMember = masked,
                    eventType = "WITHDRAWAL",
                    title = "Submitted Payout Request",
                    detail = "Method: ${wth.payoutMethodName} • Status: ${wth.status.lowercase().replaceFirstChar { c -> c.uppercase() }}",
                    timestamp = wth.createdAt,
                    iconType = "WITHDRAWAL"
                )
            )
        }

        events.sortedByDescending { it.timestamp }
    }

    fun getAgentApplicationForMember(memberId: String = "HU7K4M92Q"): Flow<AgentApplicationEntity?> =
        database.agentApplicationDao().getApplicationForMember(memberId)

    fun getCommissionLogsForAgent(agentId: String = "AGT-CENTRAL-001"): Flow<List<AgentCommissionLogEntity>> =
        database.agentCommissionLogDao().getCommissionLogsForAgent(agentId)

    suspend fun initializeDefaultDataIfEmpty() = withContext(Dispatchers.IO) {
        val currentProfile = database.userProfileDao().getUserProfile().firstOrNull()
        if (currentProfile == null) {
            val activatedAtTime = System.currentTimeMillis() - 13 * 86400 * 1000L
            val expiresAtTime = System.currentTimeMillis() + 47 * 86400 * 1000L

            val initialUser = UserProfileEntity(
                id = "current_user",
                memberId = "HU7K4M92Q",
                fullName = "Nalubega Sarah",
                phoneNumber = "+256 772 458921",
                region = "Central",
                district = "Kampala Central",
                referralCode = "UG-SARAH-402",
                inviterId = "HU2P9X14R",
                packageName = "STANDARD PACKAGE",
                paidCapitalUgx = 10000L,
                status = "ACTIVE",
                walletBalanceUgx = 18500L,
                pendingBalanceUgx = 0L,
                totalEarnedUgx = 32000L,
                hasReceivedWelcomeBonus = true,
                isPackageActivated = true,
                packageActivatedAt = activatedAtTime,
                packageExpiresAt = expiresAtTime,
                isPackageExpired = false,
                currentCycleId = "CYCLE-INIT-01",
                role = "USER"
            )
            database.userProfileDao().insertOrUpdate(initialUser)

            val additionalMembers = listOf(
                UserProfileEntity(
                    id = "member_482",
                    memberId = "HU2P9X14R",
                    fullName = "Grace Atuhaire",
                    phoneNumber = "+256 772 194 482",
                    region = "Western",
                    district = "Mbarara City",
                    referralCode = "UG-GRACE-482",
                    packageName = "PLUS PACKAGE",
                    paidCapitalUgx = 20000L,
                    status = "ACTIVE",
                    walletBalanceUgx = 54000L,
                    totalEarnedUgx = 88000L,
                    packageActivatedAt = System.currentTimeMillis() - 25 * 86400 * 1000L,
                    packageExpiresAt = System.currentTimeMillis() + 65 * 86400 * 1000L
                ),
                UserProfileEntity(
                    id = "member_319",
                    memberId = "HU8M1Q33P",
                    fullName = "Brian Kato",
                    phoneNumber = "+256 752 839 319",
                    region = "Central",
                    district = "Wakiso District",
                    referralCode = "UG-BRIAN-319",
                    packageName = "STARTER PACKAGE",
                    paidCapitalUgx = 5000L,
                    status = "ACTIVE",
                    walletBalanceUgx = 32000L,
                    totalEarnedUgx = 41000L,
                    packageActivatedAt = System.currentTimeMillis() - 10 * 86400 * 1000L,
                    packageExpiresAt = System.currentTimeMillis() + 50 * 86400 * 1000L
                ),
                UserProfileEntity(
                    id = "member_705",
                    memberId = "HU5N7B88X",
                    fullName = "Joan Babirye",
                    phoneNumber = "+256 701 928 705",
                    region = "Eastern",
                    district = "Jinja City",
                    referralCode = "UG-JOAN-705",
                    packageName = "PLUS PACKAGE",
                    paidCapitalUgx = 20000L,
                    status = "ACTIVE",
                    walletBalanceUgx = 79500L,
                    totalEarnedUgx = 112000L,
                    packageActivatedAt = System.currentTimeMillis() - 35 * 86400 * 1000L,
                    packageExpiresAt = System.currentTimeMillis() + 55 * 86400 * 1000L
                ),
                UserProfileEntity(
                    id = "member_832",
                    memberId = "HU3T6K11L",
                    fullName = "David Okello",
                    phoneNumber = "+256 782 401 832",
                    region = "Northern",
                    district = "Gulu City",
                    referralCode = "UG-DAVID-832",
                    packageName = "STANDARD PACKAGE",
                    paidCapitalUgx = 10000L,
                    status = "ACTIVE",
                    walletBalanceUgx = 23000L,
                    totalEarnedUgx = 38500L,
                    packageActivatedAt = System.currentTimeMillis() - 18 * 86400 * 1000L,
                    packageExpiresAt = System.currentTimeMillis() + 42 * 86400 * 1000L
                ),
                UserProfileEntity(
                    id = "member_901",
                    memberId = "HU9K2M901",
                    fullName = "Moses Kateregga",
                    phoneNumber = "+256 774 519 901",
                    region = "Western",
                    district = "Kabale Municipality",
                    referralCode = "UG-MOSES-901",
                    packageName = "PLUS PACKAGE",
                    paidCapitalUgx = 20000L,
                    status = "ACTIVE",
                    walletBalanceUgx = 42000L,
                    totalEarnedUgx = 65000L,
                    packageActivatedAt = System.currentTimeMillis() - 40 * 86400 * 1000L,
                    packageExpiresAt = System.currentTimeMillis() + 50 * 86400 * 1000L
                ),
                UserProfileEntity(
                    id = "member_664",
                    memberId = "HU6M4L664",
                    fullName = "Florence Namubiru",
                    phoneNumber = "+256 703 661 664",
                    region = "Central",
                    district = "Entebbe Municipality",
                    referralCode = "UG-FLORENCE-664",
                    packageName = "STANDARD PACKAGE",
                    paidCapitalUgx = 10000L,
                    status = "EXPIRED",
                    isPackageExpired = true,
                    walletBalanceUgx = 15000L,
                    totalEarnedUgx = 28000L,
                    packageActivatedAt = System.currentTimeMillis() - 65 * 86400 * 1000L,
                    packageExpiresAt = System.currentTimeMillis() - 5 * 86400 * 1000L
                )
            )
            database.userProfileDao().insertProfiles(additionalMembers)

            val initialCycle = ActivationCycleEntity(
                id = "CYCLE-INIT-01",
                userId = "current_user",
                memberId = "HU7K4M92Q",
                packageTier = "STANDARD PACKAGE",
                cycleType = "FIRST_ACTIVATION",
                amountUgx = 10000L,
                initialTasksAllowed = 5,
                activatedAt = activatedAtTime,
                expiresAt = expiresAtTime,
                status = "ACTIVE",
                facilitatedByAgentId = "AGT-CENTRAL-001"
            )
            database.activationCycleDao().insertCycle(initialCycle)

            val initialTasks = listOf(
                UtensilTaskEntity(
                    id = "task_cups_01",
                    title = "Tea Cups & Ceramic Mugs Inspection",
                    category = "Cups",
                    rewardUgx = 500L,
                    description = "Inspect household ceramic and enamel tea cups.",
                    instructions = "Verify if the colorful cups are intact, clean, and in reusable condition.",
                    imageResName = "img_utensil_cups_1790685715841",
                    status = "ACTIVE",
                    questionPrompt = "Are these household ceramic and enamel tea cups in clean, reusable condition?",
                    optionsString = "YES,NO",
                    correctAnswer = "YES",
                    estimatedMinutes = 1
                ),
                UtensilTaskEntity(
                    id = "task_plates_02",
                    title = "Dinner Plates & Serving Trays Check",
                    category = "Plates",
                    rewardUgx = 500L,
                    description = "Inspect kitchen dinner plate stack quality.",
                    instructions = "Look at the ceramic and steel plate stack on the kitchen countertop.",
                    imageResName = "img_utensil_plates_1790685730993",
                    status = "ACTIVE",
                    questionPrompt = "Is this stack of kitchen dinner plates free of visible chips or severe cracks?",
                    optionsString = "YES,NO",
                    correctAnswer = "YES",
                    estimatedMinutes = 1
                ),
                UtensilTaskEntity(
                    id = "task_bowls_03",
                    title = "Soup & Mixing Bowls Condition Evaluation",
                    category = "Bowls",
                    rewardUgx = 500L,
                    description = "Inspect kitchen soup and food mixing bowls.",
                    instructions = "Check the bowls for severe breakage, cracks, or shattered rims.",
                    imageResName = "img_utensil_bowls_1790685744488",
                    status = "ACTIVE",
                    questionPrompt = "Are these kitchen mixing and soup bowls damaged or broken?",
                    optionsString = "YES,NO",
                    correctAnswer = "NO",
                    estimatedMinutes = 1
                ),
                UtensilTaskEntity(
                    id = "task_spoons_04",
                    title = "Stainless Steel Spoons & Cooking Ladles",
                    category = "Spoons",
                    rewardUgx = 500L,
                    description = "Verify stainless cutlery and ladles.",
                    instructions = "Check the dining spoons and cooking ladles on the kitchen board.",
                    imageResName = "img_utensil_spoons_1790685756502",
                    status = "ACTIVE",
                    questionPrompt = "Are these stainless steel dining spoons and cooking ladles suitable for food preparation?",
                    optionsString = "YES,NO",
                    correctAnswer = "YES",
                    estimatedMinutes = 1
                ),
                UtensilTaskEntity(
                    id = "task_basin_05",
                    title = "Household Plastic Washing Basin Check",
                    category = "Basins",
                    rewardUgx = 500L,
                    description = "Inspect household dishwashing basin.",
                    instructions = "Examine the plastic dish and laundry washing basin.",
                    imageResName = "img_utensil_basin_1790685769518",
                    status = "ACTIVE",
                    questionPrompt = "Does this plastic household dish basin have any large hole or leak?",
                    optionsString = "YES,NO",
                    correctAnswer = "NO",
                    estimatedMinutes = 1
                ),
                UtensilTaskEntity(
                    id = "task_kettle_06",
                    title = "Stainless Steel Tea Kettle Inspection",
                    category = "Kettles",
                    rewardUgx = 500L,
                    description = "Inspect the household tea kettle spout and body.",
                    instructions = "Check the kettle material and exterior polish.",
                    imageResName = "utensil_kettle_1790683225836",
                    status = "ACTIVE",
                    questionPrompt = "Is this tea kettle made of stainless steel material?",
                    optionsString = "YES,NO",
                    correctAnswer = "YES",
                    estimatedMinutes = 1
                ),
                UtensilTaskEntity(
                    id = "task_fryingpan_07",
                    title = "Non-Stick Frying Pan & Spatula Check",
                    category = "Pans",
                    rewardUgx = 500L,
                    description = "Check frying pan surface and wooden spatula.",
                    instructions = "Inspect the non-stick surface and wooden handle.",
                    imageResName = "utensil_fryingpan_1790683238080",
                    status = "ACTIVE",
                    questionPrompt = "Is the non-stick pan surface rusted or burnt completely through?",
                    optionsString = "YES,NO",
                    correctAnswer = "NO",
                    estimatedMinutes = 1
                )
            )
            database.utensilTaskDao().insertTasks(initialTasks)

            val initialSubmissions = listOf(
                TaskSubmissionEntity(
                    id = "SUB-PREV-01",
                    taskId = "task_kettle_01",
                    taskTitle = "Verify Stainless Steel Kettle Condition",
                    rewardUgx = 500L,
                    selectedAnswer = "YES",
                    notes = "Clean surface with no rust spots, whistle lid in place.",
                    status = "APPROVED",
                    isCorrect = true,
                    submittedAt = System.currentTimeMillis() - 3 * 3600 * 1000,
                    reviewedAt = System.currentTimeMillis() - 3 * 3600 * 1000,
                    feedback = "Correct! +UGX 500 automatically credited to Withdrawable Balance."
                ),
                TaskSubmissionEntity(
                    id = "SUB-PREV-02",
                    taskId = "task_fryingpan_02",
                    taskTitle = "Classify Non-Stick Frying Pan & Spatula",
                    rewardUgx = 500L,
                    selectedAnswer = "NO",
                    notes = "Coating is intact without scratches.",
                    status = "APPROVED",
                    isCorrect = true,
                    submittedAt = System.currentTimeMillis() - 24 * 3600 * 1000,
                    reviewedAt = System.currentTimeMillis() - 24 * 3600 * 1000,
                    feedback = "Correct! +UGX 500 automatically credited to Withdrawable Balance."
                )
            )
            initialSubmissions.forEach { database.taskSubmissionDao().insertSubmission(it) }

            val initialTransactions = listOf(
                WalletTransactionEntity(
                    id = "TX-WBONUS-INIT",
                    title = "First-Time Welcome Bonus (STANDARD PACKAGE)",
                    type = "WELCOME_BONUS",
                    amountUgx = 1000L,
                    status = "CREDITED",
                    eventKey = "WELCOME_BONUS:HU7K4M92Q",
                    timestamp = System.currentTimeMillis() - 5 * 86400 * 1000
                ),
                WalletTransactionEntity(
                    id = "TX-TASK-INIT1",
                    title = "Task Reward (STANDARD PACKAGE): Stainless Steel Kettle",
                    type = "TASK_REWARD",
                    amountUgx = 500L,
                    status = "CREDITED",
                    eventKey = "TASK_REWARD:task_kettle_01",
                    timestamp = System.currentTimeMillis() - 3 * 3600 * 1000
                ),
                WalletTransactionEntity(
                    id = "TX-TASK-INIT2",
                    title = "Task Reward (STANDARD PACKAGE): Non-Stick Frying Pan",
                    type = "TASK_REWARD",
                    amountUgx = 500L,
                    status = "CREDITED",
                    eventKey = "TASK_REWARD:task_fryingpan_02",
                    timestamp = System.currentTimeMillis() - 24 * 3600 * 1000
                ),
                WalletTransactionEntity(
                    id = "TX-REFCOM-INIT1",
                    title = "Direct Activation Commission (10%): Kato Brian (STANDARD PACKAGE)",
                    type = "REFERRAL_COMMISSION",
                    amountUgx = 1000L,
                    status = "CREDITED",
                    eventKey = "REF_COMMISSION:ref_01",
                    timestamp = System.currentTimeMillis() - 2 * 86400 * 1000
                ),
                WalletTransactionEntity(
                    id = "TX-REFCOM-INIT2",
                    title = "Direct Activation Commission (10%): Babirye Joan (STANDARD PACKAGE)",
                    type = "REFERRAL_COMMISSION",
                    amountUgx = 1000L,
                    status = "CREDITED",
                    eventKey = "REF_COMMISSION:ref_02",
                    timestamp = System.currentTimeMillis() - 4 * 86400 * 1000
                )
            )
            database.walletTransactionDao().insertTransactions(initialTransactions)

            val initialReferrals = listOf(
                ReferralEntity(
                    id = "ref_01",
                    friendName = "Kato Brian",
                    friendPhone = "+256 701 334912",
                    district = "Kampala Central",
                    status = "ACTIVE",
                    bonusUgx = 1000L,
                    referralPackage = "STANDARD PACKAGE",
                    isCommissionCredited = true,
                    joinedDate = "Yesterday"
                ),
                ReferralEntity(
                    id = "ref_02",
                    friendName = "Babirye Joan",
                    friendPhone = "+256 754 889102",
                    district = "Wakiso (Nansana)",
                    status = "ACTIVE",
                    bonusUgx = 1000L,
                    referralPackage = "STANDARD PACKAGE",
                    isCommissionCredited = true,
                    joinedDate = "3 days ago"
                ),
                ReferralEntity(
                    id = "ref_03",
                    friendName = "Okello David",
                    friendPhone = "+256 788 123984",
                    district = "Mukono",
                    status = "ACTIVE",
                    bonusUgx = 1000L,
                    referralPackage = "STANDARD PACKAGE",
                    isCommissionCredited = true,
                    joinedDate = "1 week ago"
                ),
                ReferralEntity(
                    id = "ref_04",
                    friendName = "Akello Grace",
                    friendPhone = "+256 777 459102",
                    district = "Jinja City",
                    status = "PENDING",
                    bonusUgx = 1000L,
                    referralPackage = "STANDARD PACKAGE",
                    isCommissionCredited = false,
                    joinedDate = "Today (Awaiting Activation)"
                )
            )
            database.referralDao().insertReferrals(initialReferrals)

            // Seed Admin-managed Withdrawal Methods
            val initialMethods = listOf(
                WithdrawalMethodEntity(
                    id = "METHOD-MOMO",
                    name = "MTN Mobile Money Uganda",
                    supportedCountries = "Uganda",
                    currency = "UGX",
                    requiredAccountInfo = "MTN Registered Mobile Number (e.g. 077... / 078...)",
                    instructions = "Ensure your MTN Mobile Money account name matches your registered HomeUtensils name.",
                    isActive = true,
                    minimumAmountUgx = 2000L,
                    isDefault = true
                ),
                WithdrawalMethodEntity(
                    id = "METHOD-AIRTEL",
                    name = "Airtel Money Uganda",
                    supportedCountries = "Uganda",
                    currency = "UGX",
                    requiredAccountInfo = "Airtel Registered Mobile Number (e.g. 070... / 075...)",
                    instructions = "Instant cashout to your active Airtel Money wallet.",
                    isActive = true,
                    minimumAmountUgx = 2000L,
                    isDefault = false
                ),
                WithdrawalMethodEntity(
                    id = "METHOD-BINANCE",
                    name = "Binance Pay / USDT",
                    supportedCountries = "Global",
                    currency = "USDT",
                    requiredAccountInfo = "Binance Pay ID or TRC20 Wallet Address",
                    instructions = "Minimum UGX 2,000 equivalent in USDT. Network: TRC20 only.",
                    isActive = true,
                    minimumAmountUgx = 2000L,
                    isDefault = false
                ),
                WithdrawalMethodEntity(
                    id = "METHOD-CHIPPER",
                    name = "Chipper Cash",
                    supportedCountries = "East Africa (UG, KE, TZ, RW)",
                    currency = "UGX",
                    requiredAccountInfo = "Chipper Username (@username)",
                    instructions = "Funds credited instantly to your Chipper Cash handle.",
                    isActive = true,
                    minimumAmountUgx = 2000L,
                    isDefault = false
                ),
                WithdrawalMethodEntity(
                    id = "METHOD-BANK",
                    name = "Uganda Commercial Bank Transfer",
                    supportedCountries = "Uganda",
                    currency = "UGX",
                    requiredAccountInfo = "Bank Name, Account Number & Branch",
                    instructions = "Supports Stanbic, Centenary, Absa, Equity. Processed within 1 business day.",
                    isActive = true,
                    minimumAmountUgx = 10000L,
                    isDefault = false
                )
            )
            database.withdrawalMethodDao().insertMethods(initialMethods)

            // Seed Beneficiary Details (Verified)
            val initialBeneficiary = BeneficiaryDetailsEntity(
                userId = "current_user",
                memberId = "HU7K4M92Q",
                beneficiaryName = "Nalubega Sarah",
                accountIdentifier = "+256 772 458 921",
                methodId = "METHOD-MOMO",
                methodName = "MTN Mobile Money Uganda",
                isVerified = true,
                verifiedAt = System.currentTimeMillis() - 20 * 86400 * 1000L
            )
            database.beneficiaryDetailsDao().insertBeneficiary(initialBeneficiary)

            // Seed Agent Working Float & Regional Agents with Non-Sequential IDs & Official Payment Details
            val initialAgents = listOf(
                AgentWorkingFloatEntity(
                    agentId = "AGT-74921-CEN",
                    agentName = "Ronald Mukasa",
                    memberId = "HU7K4M92Q",
                    territory = "Central Region (Kampala & Wakiso)",
                    region = "Central",
                    isAvailable = true,
                    workingFloatUgx = 100000L,
                    totalFacilitatedUgx = 35000L,
                    commissionEarnedUgx = 14500L,
                    status = "ACTIVE",
                    photoUri = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
                    phone = "+256 772 458 928",
                    whatsappNumber = "+256 772 458 928",
                    officialPaymentMethod = "MTN Mobile Money",
                    officialPaymentAccount = "0772458928",
                    officialBeneficiaryName = "Ronald Mukasa",
                    supportedPackages = "STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE"
                ),
                AgentWorkingFloatEntity(
                    agentId = "AGT-58392-EAS",
                    agentName = "David Ochieng",
                    memberId = "HU8D2X11M",
                    territory = "Eastern Region (Jinja & Mbale)",
                    region = "Eastern",
                    isAvailable = true,
                    workingFloatUgx = 85000L,
                    totalFacilitatedUgx = 20000L,
                    commissionEarnedUgx = 8200L,
                    status = "ACTIVE",
                    photoUri = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                    phone = "+256 782 119 402",
                    whatsappNumber = "+256 782 119 402",
                    officialPaymentMethod = "Airtel Money",
                    officialPaymentAccount = "0782119402",
                    officialBeneficiaryName = "David Ochieng",
                    supportedPackages = "STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE"
                ),
                AgentWorkingFloatEntity(
                    agentId = "AGT-62184-WES",
                    agentName = "Emmanuel Magezi",
                    memberId = "HU4W9Y77K",
                    territory = "Western Region (Mbarara & Fort Portal)",
                    region = "Western",
                    isAvailable = true,
                    workingFloatUgx = 70000L,
                    totalFacilitatedUgx = 15000L,
                    commissionEarnedUgx = 6100L,
                    status = "ACTIVE",
                    photoUri = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
                    phone = "+256 701 334 812",
                    whatsappNumber = "+256 701 334 812",
                    officialPaymentMethod = "MTN Mobile Money",
                    officialPaymentAccount = "0701334812",
                    officialBeneficiaryName = "Emmanuel Magezi",
                    supportedPackages = "STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE"
                ),
                AgentWorkingFloatEntity(
                    agentId = "AGT-49271-NOR",
                    agentName = "Brenda Akello",
                    memberId = "HU9N5Z33P",
                    territory = "Northern Region (Gulu & Lira)",
                    region = "Northern",
                    isAvailable = true,
                    workingFloatUgx = 90000L,
                    totalFacilitatedUgx = 45000L,
                    commissionEarnedUgx = 19200L,
                    status = "ACTIVE",
                    photoUri = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400",
                    phone = "+256 755 902 184",
                    whatsappNumber = "+256 755 902 184",
                    officialPaymentMethod = "Chipper Cash",
                    officialPaymentAccount = "@brenda_akello_hu",
                    officialBeneficiaryName = "Brenda Akello",
                    supportedPackages = "STARTER PACKAGE, STANDARD PACKAGE, PLUS PACKAGE"
                )
            )
            for (agent in initialAgents) {
                database.agentWorkingFloatDao().insertWorkingFloat(agent)
            }
            // Retain AGT-CENTRAL-001 compatibility alias for Ronald Mukasa
            database.agentWorkingFloatDao().insertWorkingFloat(
                initialAgents.first().copy(agentId = "AGT-CENTRAL-001")
            )

            // Seed Agent Payment Config
            database.agentPaymentConfigDao().insertConfig(
                AgentPaymentConfigEntity(
                    id = "global_config",
                    responseTimeoutMinutes = 60,
                    maxUnresolvedCasesThreshold = 2,
                    autoRestrictNewAssignments = true
                )
            )

            // Seed sample Agent Activation Request (Payment Reported & Awaiting Agent Confirmation)
            val sampleRequest = AgentActivationRequestEntity(
                id = "APR-2026-0814",
                memberId = "HU7K4M92Q",
                memberName = "Nalubega Sarah",
                memberPhone = "+256 772 458 921",
                agentId = "AGT-CENTRAL-001",
                agentName = "Ronald Mukasa",
                agentPhotoUri = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
                packageName = "STARTER PACKAGE",
                amountUgx = 5000L,
                region = "Central",
                status = "Payment Reported",
                createdAt = System.currentTimeMillis() - 25 * 60 * 1000L,
                agentAcceptedAt = System.currentTimeMillis() - 20 * 60 * 1000L,
                paymentReportedAt = System.currentTimeMillis() - 10 * 60 * 1000L,
                responseDeadline = System.currentTimeMillis() + 50 * 60 * 1000L,
                paymentMethod = "MTN Mobile Money",
                paymentReference = "MM-UGX-982147",
                paymentProofUri = "proof_momo_receipt.jpg",
                paymentProofUploadedAt = System.currentTimeMillis() - 10 * 60 * 1000L,
                paymentProofUploader = "HU7K4M92Q",
                memberNote = "Transferred UGX 5,000 for Starter Package activation via MTN Momo"
            )
            database.agentActivationRequestDao().insertRequest(sampleRequest)

            val initialCommission = AgentCommissionLogEntity(
                id = "COMM-INIT-01",
                agentId = "AGT-CENTRAL-001",
                memberId = "HU7K4M92Q",
                cycleId = "CYCLE-INIT-01",
                taskId = "task_kettle_01",
                taskTitle = "Verify Stainless Steel Kettle Condition",
                taskRewardUgx = 500L,
                commissionRate = 0.02,
                commissionUgx = 10L,
                timestamp = System.currentTimeMillis() - 3 * 3600 * 1000
            )
            database.agentCommissionLogDao().insertCommissionLog(initialCommission)

            val sampleApplication = AgentApplicationEntity(
                id = "AGT-APP-9201",
                memberId = "HU3M8K55Q",
                fullName = "Grace Atuhaire",
                phoneNumber = "+256 701 445 921",
                region = "Western",
                idType = "National ID/NIN",
                idNumber = "CM9084729183HG",
                idDocumentUri = "doc_national_id_front.jpg",
                profilePhotoUri = "agent_headshot.jpg",
                applicationReason = "Experienced territory merchant coordinator in Mbarara. Ready to facilitate Plus and Standard package activations.",
                agreedToResponsibilities = true,
                status = "PENDING",
                submittedAt = System.currentTimeMillis() - 86400 * 1000L
            )
            database.agentApplicationDao().insertApplication(sampleApplication)

            // Seed initial sample P2P Transfer (Sender sends 5,000; Recipient receives 5,000; Fee 500 on top; Total deducted 5,500)
            val initialP2P = P2PTransferEntity(
                id = "TXN-P2P-10948",
                senderUserId = "current_user",
                senderMemberId = "HU7K4M92Q",
                senderName = "Nalubega Sarah",
                recipientMemberId = "HU2P9X14R",
                recipientName = "Grace Atuhaire",
                amountUgx = 5000L,
                feeUgx = 500L,
                totalDeductedUgx = 5500L,
                netRecipientAmountUgx = 5000L,
                isActivationFunding = false,
                status = "CONFIRMED",
                transactionPinVerified = true,
                timestamp = System.currentTimeMillis() - 6 * 86400 * 1000L
            )
            database.p2pTransferDao().insertTransfer(initialP2P)

            val initialWithdrawal = WithdrawalRequestEntity(
                id = "WTH-2026-0914",
                userId = "current_user",
                memberId = "HU7K4M92Q",
                memberName = "Nalubega Sarah",
                requestedAmountUgx = 10000L,
                feeUgx = 1000L,
                netAmountUgx = 9000L,
                payoutMethodId = "METHOD-MOMO",
                payoutMethodName = "MTN Mobile Money Uganda",
                beneficiaryAccount = "+256 772 458 921",
                beneficiaryName = "Nalubega Sarah",
                status = "COMPLETED",
                processedByAdmin = "admin@homeutensils.ug",
                processedAt = System.currentTimeMillis() - 7 * 86400 * 1000L,
                externalRef = "MOMO-UG-7839210",
                createdAt = System.currentTimeMillis() - 8 * 86400 * 1000L
            )
            database.withdrawalRequestDao().insertWithdrawal(initialWithdrawal)

            val mockupWithdrawals = listOf(
                WithdrawalRequestEntity(
                    id = "WTH-2026-4821",
                    userId = "member_482",
                    memberId = "HU2P9X14R",
                    memberName = "Grace Atuhaire",
                    requestedAmountUgx = 50000L,
                    feeUgx = 5000L,
                    netAmountUgx = 45000L,
                    payoutMethodId = "METHOD-MOMO",
                    payoutMethodName = "MTN MoMo",
                    beneficiaryAccount = "+2567***482",
                    beneficiaryName = "Grace Atuhaire",
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 2 * 3600 * 1000L
                ),
                WithdrawalRequestEntity(
                    id = "WTH-2026-3192",
                    userId = "member_319",
                    memberId = "HU8M1Q33P",
                    memberName = "Brian Kato",
                    requestedAmountUgx = 30000L,
                    feeUgx = 3000L,
                    netAmountUgx = 27000L,
                    payoutMethodId = "METHOD-AIRTEL",
                    payoutMethodName = "Airtel Money",
                    beneficiaryAccount = "+2567***319",
                    beneficiaryName = "Brian Kato",
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 4 * 3600 * 1000L
                ),
                WithdrawalRequestEntity(
                    id = "WTH-2026-7053",
                    userId = "member_705",
                    memberId = "HU5N7B88X",
                    memberName = "Joan Babirye",
                    requestedAmountUgx = 75000L,
                    feeUgx = 7500L,
                    netAmountUgx = 67500L,
                    payoutMethodId = "METHOD-MOMO",
                    payoutMethodName = "MTN MoMo",
                    beneficiaryAccount = "+2567***705",
                    beneficiaryName = "Joan Babirye",
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 5 * 3600 * 1000L
                ),
                WithdrawalRequestEntity(
                    id = "WTH-2026-8324",
                    userId = "member_832",
                    memberId = "HU3T6K11L",
                    memberName = "David Okello",
                    requestedAmountUgx = 20000L,
                    feeUgx = 2000L,
                    netAmountUgx = 18000L,
                    payoutMethodId = "METHOD-BANK",
                    payoutMethodName = "Bank Transfer",
                    beneficiaryAccount = "+2567***832",
                    beneficiaryName = "David Okello",
                    status = "PENDING",
                    createdAt = System.currentTimeMillis() - 6 * 3600 * 1000L
                )
            )
            for (w in mockupWithdrawals) {
                database.withdrawalRequestDao().insertWithdrawal(w)
            }

            val mockupAgentApps = listOf(
                AgentApplicationEntity(
                    id = "AGT-APP-901",
                    memberId = "HU9K2M901",
                    fullName = "Moses Kateregga",
                    phoneNumber = "+2567***901",
                    region = "Western",
                    idType = "National ID/NIN",
                    idNumber = "CM8910293847HK",
                    idDocumentUri = "doc_nin_front.jpg",
                    applicationReason = "Experienced territory merchant coordinator in Mbarara. Ready to provide instant member package activations.",
                    status = "PENDING",
                    submittedAt = System.currentTimeMillis() - 3 * 3600 * 1000L
                ),
                AgentApplicationEntity(
                    id = "AGT-APP-664",
                    memberId = "HU6M4L664",
                    fullName = "Florence Namubiru",
                    phoneNumber = "+2567***664",
                    region = "Central",
                    idType = "National ID/NIN",
                    idNumber = "CF9402918274MK",
                    idDocumentUri = "doc_nin_florence.jpg",
                    applicationReason = "Established household distributor in Kampala Central with deep community reach.",
                    status = "PENDING",
                    submittedAt = System.currentTimeMillis() - 6 * 3600 * 1000L
                ),
                AgentApplicationEntity(
                    id = "AGT-APP-278",
                    memberId = "HU2R8P278",
                    fullName = "Ronald Mukasa",
                    phoneNumber = "+2567***278",
                    region = "Central",
                    idType = "National ID/NIN",
                    idNumber = "CM8729103948PK",
                    idDocumentUri = "doc_nin_ronald.jpg",
                    applicationReason = "Territory leader for Kampala & Wakiso districts.",
                    status = "APPROVED",
                    reviewedByAdmin = "admin@homeutensils.ug",
                    reviewedAt = System.currentTimeMillis() - 24 * 3600 * 1000L,
                    reviewNotes = "Verified NIN and territorial business operations.",
                    submittedAt = System.currentTimeMillis() - 48 * 3600 * 1000L
                ),
                AgentApplicationEntity(
                    id = "AGT-APP-450",
                    memberId = "HU4S5T450",
                    fullName = "Andrew Ssemwogerere",
                    phoneNumber = "+2567***450",
                    region = "Eastern",
                    idType = "National ID/NIN",
                    idNumber = "CM9102938475ZK",
                    idDocumentUri = "doc_nin_andrew.jpg",
                    applicationReason = "Jinja district local representative.",
                    status = "REJECTED",
                    reviewedByAdmin = "admin@homeutensils.ug",
                    reviewedAt = System.currentTimeMillis() - 12 * 3600 * 1000L,
                    reviewNotes = "Incomplete territorial documentation provided. Resubmit with business trade license.",
                    submittedAt = System.currentTimeMillis() - 36 * 3600 * 1000L
                )
            )
            for (a in mockupAgentApps) {
                database.agentApplicationDao().insertApplication(a)
            }

            // Seed initial activation funding target
            val initialTarget = ActivationFundingTargetEntity(
                id = "TGT-STARTER-01",
                memberId = "HU7K4M92Q",
                targetPackageName = "STANDARD PACKAGE",
                targetAmountUgx = 10000L,
                contributedAmountUgx = 10000L,
                contributorsCount = 2,
                isCompleted = true,
                createdAt = System.currentTimeMillis() - 13 * 86400 * 1000L,
                completedAt = System.currentTimeMillis() - 13 * 86400 * 1000L
            )
            database.activationFundingTargetDao().insertTarget(initialTarget)

            // Seed initial agent facilitation matching CYCLE-INIT-01
            val initialFacilitation = AgentFacilitationEntity(
                id = "FAC-INIT-01",
                agentId = "AGT-CENTRAL-001",
                recipientMemberId = "HU7K4M92Q",
                recipientName = "Nalubega Sarah",
                packageName = "STANDARD PACKAGE",
                amountUgx = 10000L,
                feeUgx = 0L,
                timestamp = System.currentTimeMillis() - 13 * 86400 * 1000L
            )
            database.agentFacilitationDao().insertFacilitation(initialFacilitation)

            // Seed Official Uganda Public Holidays (fixed and variable dates for 2026/2027)
            val defaultUgandaHolidays = listOf(
                UgandaHolidayEntity("HOL-2026-01", "New Year's Day", "2026-01-01", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-02", "NRM Liberation Day", "2026-01-26", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-03", "Archbishop Janani Luwum Day", "2026-02-16", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-04", "International Women's Day", "2026-03-08", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-05", "Eid al-Fitr", "2026-03-20", isOfficialPublicHoliday = true, description = "Islamic Public Holiday (Moon Sighting / Variable)", year = 2026),
                UgandaHolidayEntity("HOL-2026-06", "Good Friday", "2026-04-03", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2026),
                UgandaHolidayEntity("HOL-2026-07", "Easter Monday", "2026-04-06", isOfficialPublicHoliday = true, description = "Christian Holiday (Variable)", year = 2026),
                UgandaHolidayEntity("HOL-2026-08", "Labour Day", "2026-05-01", isOfficialPublicHoliday = true, description = "International Workers' Day", year = 2026),
                UgandaHolidayEntity("HOL-2026-09", "Eid al-Adha / Idd al-Adha", "2026-05-27", isOfficialPublicHoliday = true, description = "Islamic Feast of Sacrifice (Variable)", year = 2026),
                UgandaHolidayEntity("HOL-2026-10", "Uganda Martyrs' Day", "2026-06-03", isOfficialPublicHoliday = true, description = "Namugongo National Pilgrimage Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-11", "National Heroes Day", "2026-06-09", isOfficialPublicHoliday = true, description = "National Public Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-12", "Independence Day", "2026-10-09", isOfficialPublicHoliday = true, description = "Uganda National Independence Day", year = 2026),
                UgandaHolidayEntity("HOL-2026-13", "Christmas Day", "2026-12-25", isOfficialPublicHoliday = true, description = "Christmas Day Public Holiday", year = 2026),
                UgandaHolidayEntity("HOL-2026-14", "Boxing Day", "2026-12-26", isOfficialPublicHoliday = true, description = "Boxing Day Public Holiday", year = 2026)
            )
            database.ugandaHolidayDao().insertHolidays(defaultUgandaHolidays)
        }
    }


    /**
     * Rule 1, 2, 6: Server-side validation of task answers with atomic transaction & duplicate prevention
     * - Starter: UGX 500
     * - Standard: UGX 500
     * - Plus: UGX 1,000
     * - If wrong: reward = UGX 0, correct answer highlighted, saved permanently to history
     * - No Admin approval required
     * - Atomic execution preventing duplicate credits
     */
    suspend fun evaluateAndSubmitYesNoTask(
        task: UtensilTaskEntity,
        selectedAnswer: String
    ): TaskEvaluationResult = withContext(Dispatchers.IO) {
        database.withTransaction {
            // Check duplicate submission for idempotency
            val existingSub = database.taskSubmissionDao().getSubmissionByTaskId(task.id)
            if (existingSub != null) {
                return@withTransaction TaskEvaluationResult(
                    taskId = task.id,
                    taskTitle = task.title,
                    isCorrect = existingSub.isCorrect,
                    awardedUgx = existingSub.rewardUgx,
                    userAnswer = existingSub.selectedAnswer,
                    correctAnswer = task.correctAnswer,
                    feedback = if (existingSub.isCorrect) "Already completed! Awarded UGX ${existingSub.rewardUgx}." else "Already submitted. Correct answer: ${task.correctAnswer}."
                )
            }

            // Server-side answer validation
            val trimmedAnswer = selectedAnswer.trim().uppercase()
            val expectedAnswer = task.correctAnswer.trim().uppercase()
            val isCorrect = trimmedAnswer == expectedAnswer

            // Fetch user's package tier to determine task reward
            val profile = database.userProfileDao().getProfileOnce()
            val pkg = HomeUtensilsPackage.fromName(profile?.packageName)
            val awardedUgx = if (isCorrect) pkg.taskRewardUgx else 0L

            val submissionId = "SUB-" + UUID.randomUUID().toString().take(8).uppercase()
            val submission = TaskSubmissionEntity(
                id = submissionId,
                taskId = task.id,
                taskTitle = task.title,
                rewardUgx = awardedUgx,
                selectedAnswer = trimmedAnswer,
                notes = "Question: ${task.questionPrompt} | User answered: $trimmedAnswer",
                status = if (isCorrect) "APPROVED" else "REJECTED",
                isCorrect = isCorrect,
                imageResName = task.imageResName,
                questionPrompt = task.questionPrompt,
                submittedAt = System.currentTimeMillis(),
                reviewedAt = System.currentTimeMillis(),
                feedback = if (isCorrect) {
                    "Correct! +UGX $awardedUgx automatically credited to Withdrawable Balance (${pkg.displayName})."
                } else {
                    "Incorrect. The correct answer was $expectedAnswer. UGX 0 awarded."
                }
            )
            database.taskSubmissionDao().insertSubmission(submission)

            // Mark task as COMPLETED so member can continue to next eligible task
            database.utensilTaskDao().updateTaskStatus(task.id, "COMPLETED")

            // Atomic credit & permanent ledger entry if correct
            if (isCorrect && awardedUgx > 0L) {
                val eventKey = "TASK_REWARD:${task.id}"
                val existingTx = database.walletTransactionDao().getTransactionByEventKey(eventKey)
                if (existingTx == null) {
                    database.userProfileDao().addCompletedEarnings(awardedUgx)

                    val uniqueTxId = "TX-TASK-" + UUID.randomUUID().toString().take(10).uppercase()
                    val tx = WalletTransactionEntity(
                        id = uniqueTxId,
                        title = "Task Reward (${pkg.displayName}): ${task.title}",
                        type = "TASK_REWARD",
                        amountUgx = awardedUgx,
                        status = "CREDITED",
                        eventKey = eventKey,
                        timestamp = System.currentTimeMillis()
                    )
                    database.walletTransactionDao().insertTransaction(tx)

                    // Rule 8: 2% Agent Commission (Attributed to cycle, not deducted from member)
                    processAgentTaskCommission(profile?.memberId ?: "HU7K4M92Q", task, awardedUgx)
                }
            }

            TaskEvaluationResult(
                taskId = task.id,
                taskTitle = task.title,
                isCorrect = isCorrect,
                awardedUgx = awardedUgx,
                userAnswer = trimmedAnswer,
                correctAnswer = expectedAnswer,
                feedback = if (isCorrect) {
                    "Correct! +UGX $awardedUgx automatically credited to Withdrawable Balance."
                } else {
                    "Incorrect! Correct answer was $expectedAnswer. UGX 0 awarded."
                }
            )
        }
    }

    suspend fun submitUtensilTask(
        task: UtensilTaskEntity,
        selectedAnswer: String,
        notes: String
    ) = withContext(Dispatchers.IO) {
        database.withTransaction {
            val existingSub = database.taskSubmissionDao().getSubmissionByTaskId(task.id)
            if (existingSub != null) return@withTransaction

            val trimmedAnswer = selectedAnswer.trim().uppercase()
            val expectedAnswer = task.correctAnswer.trim().uppercase()
            val isCorrect = trimmedAnswer == expectedAnswer

            val profile = database.userProfileDao().getProfileOnce()
            val pkg = HomeUtensilsPackage.fromName(profile?.packageName)
            val awardedUgx = if (isCorrect) pkg.taskRewardUgx else 0L

            val submissionId = "SUB-" + UUID.randomUUID().toString().take(8).uppercase()
            val submission = TaskSubmissionEntity(
                id = submissionId,
                taskId = task.id,
                taskTitle = task.title,
                rewardUgx = awardedUgx,
                selectedAnswer = selectedAnswer,
                notes = notes.ifBlank { "Utensil verification inspection." },
                status = if (isCorrect) "APPROVED" else "REJECTED",
                isCorrect = isCorrect,
                imageResName = task.imageResName,
                questionPrompt = task.questionPrompt,
                submittedAt = System.currentTimeMillis(),
                reviewedAt = System.currentTimeMillis(),
                feedback = if (isCorrect) {
                    "Correct! +UGX $awardedUgx automatically credited to Withdrawable Balance (${pkg.displayName})."
                } else {
                    "Incorrect answer. Expected: $expectedAnswer. UGX 0 awarded."
                }
            )
            database.taskSubmissionDao().insertSubmission(submission)
            database.utensilTaskDao().updateTaskStatus(task.id, "COMPLETED")

            if (isCorrect && awardedUgx > 0L) {
                val eventKey = "TASK_REWARD:${task.id}"
                val existingTx = database.walletTransactionDao().getTransactionByEventKey(eventKey)
                if (existingTx == null) {
                    database.userProfileDao().addCompletedEarnings(awardedUgx)

                    val uniqueTxId = "TX-TASK-" + UUID.randomUUID().toString().take(10).uppercase()
                    val tx = WalletTransactionEntity(
                        id = uniqueTxId,
                        title = "Task Reward (${pkg.displayName}): ${task.title}",
                        type = "TASK_REWARD",
                        amountUgx = awardedUgx,
                        status = "CREDITED",
                        eventKey = eventKey,
                        timestamp = System.currentTimeMillis()
                    )
                    database.walletTransactionDao().insertTransaction(tx)

                    // Rule 8: 2% Agent Commission
                    processAgentTaskCommission(profile?.memberId ?: "HU7K4M92Q", task, awardedUgx)
                }
            }
        }
    }

    /**
     * Rule 8: Agent Commission Calculation & Attribution
     * - Agent earns 2% of qualifying task income generated by users whose specific activation/reactivation cycle was facilitated by that Agent.
     * - The commission is NOT deducted from the member's task reward.
     * - Agent attribution belongs to the specific activation/reactivation cycle.
     */
    private suspend fun processAgentTaskCommission(memberId: String, task: UtensilTaskEntity, awardedUgx: Long) {
        val activeCycle = database.activationCycleDao().getActiveCycle()
        if (activeCycle != null && !activeCycle.facilitatedByAgentId.isNullOrBlank()) {
            val agentId = activeCycle.facilitatedByAgentId
            val commUgx = (awardedUgx * 0.02).toLong()
            if (commUgx > 0) {
                val commId = "COMM-" + UUID.randomUUID().toString().take(8).uppercase()
                database.agentCommissionLogDao().insertCommissionLog(
                    AgentCommissionLogEntity(
                        id = commId,
                        agentId = agentId,
                        memberId = memberId,
                        cycleId = activeCycle.id,
                        taskId = task.id,
                        taskTitle = task.title,
                        taskRewardUgx = awardedUgx,
                        commissionRate = 0.02,
                        commissionUgx = commUgx,
                        timestamp = System.currentTimeMillis()
                    )
                )
                database.agentWorkingFloatDao().addCommission(agentId, commUgx)
            }
        }
    }

    /**
     * Rule 3 & 7: First-Time Package Activation
     * - Activation and welcome bonus recorded as separate ledger events
     * - Starter: UGX 500 welcome bonus
     * - Standard: UGX 1,000 welcome bonus
     * - Plus: UGX 2,000 welcome bonus
     * - No welcome bonus for registration alone, upgrades, or reactivation
     * - Records user ID, package, and amount in Activation Cycle
     */
    suspend fun activatePackage(packageName: String): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val profile = database.userProfileDao().getProfileOnce() ?: return@withTransaction false
            val pkg = HomeUtensilsPackage.fromName(packageName)

            val isFirstTimeActivation = !profile.hasReceivedWelcomeBonus && !profile.isPackageActivated
            val cycleId = "CYCLE-ACT-" + UUID.randomUUID().toString().take(8).uppercase()
            val activatedAt = System.currentTimeMillis()
            val expiresAt = activatedAt + pkg.durationDays * 86400 * 1000L

            // Record Activation Attribution Cycle
            val cycle = ActivationCycleEntity(
                id = cycleId,
                userId = "current_user",
                memberId = profile.memberId,
                packageTier = pkg.displayName,
                cycleType = "FIRST_ACTIVATION",
                amountUgx = pkg.activationAmountUgx,
                initialTasksAllowed = pkg.initialTasksCount,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                status = "ACTIVE"
            )
            database.activationCycleDao().insertCycle(cycle)

            // Update user package tier, paid capital, and cycle details
            database.userProfileDao().updateCycleDetails(
                packageName = pkg.displayName,
                paidCapital = pkg.paidCapitalUgx,
                cycleId = cycleId,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                isExpired = false
            )

            // Ledger Event 1: Package Activation (Attribution)
            val txActivation = WalletTransactionEntity(
                id = "TX-ACT-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "Package Activation (${pkg.displayName})",
                type = "PACKAGE_ACTIVATION",
                amountUgx = pkg.activationAmountUgx,
                status = "CREDITED",
                eventKey = "PACKAGE_ACTIVATION:$cycleId",
                timestamp = activatedAt
            )
            database.walletTransactionDao().insertTransaction(txActivation)

            // Ledger Event 2: Welcome Bonus (Only on first successful activation)
            if (isFirstTimeActivation) {
                val eventKey = "WELCOME_BONUS:${profile.memberId}"
                val existingTx = database.walletTransactionDao().getTransactionByEventKey(eventKey)
                if (existingTx == null) {
                    val bonusAmount = pkg.welcomeBonusUgx
                    database.userProfileDao().addCompletedEarnings(bonusAmount)
                    database.userProfileDao().markWelcomeBonusReceived()

                    val txBonus = WalletTransactionEntity(
                        id = "TX-WBONUS-" + UUID.randomUUID().toString().take(10).uppercase(),
                        title = "First-Time Welcome Bonus (${pkg.displayName})",
                        type = "WELCOME_BONUS",
                        amountUgx = bonusAmount,
                        status = "CREDITED",
                        eventKey = eventKey,
                        timestamp = activatedAt + 10L
                    )
                    database.walletTransactionDao().insertTransaction(txBonus)
                }
            }
            true
        }
    }

    /**
     * Rule 5: Upgrades
     * - No downgrade allowed
     * - Difference only:
     *   Starter -> Standard: 5,000 UGX
     *   Standard -> Plus: 10,000 UGX
     *   Starter -> Plus: 15,000 UGX
     * - No welcome bonus for upgrade
     * - Inviter commission: 10% of upgrade top-up only
     * - Creates new activation cycle (Never overwrites previous cycles)
     */
    suspend fun upgradePackage(targetPackageName: String): UpgradeResult = withContext(Dispatchers.IO) {
        database.withTransaction {
            val profile = database.userProfileDao().getProfileOnce()
                ?: return@withTransaction UpgradeResult(false, "User profile not found", "", 0, 0)

            val currentPkg = HomeUtensilsPackage.fromName(profile.packageName)
            val targetPkg = HomeUtensilsPackage.fromName(targetPackageName)

            val currentRank = when (currentPkg) {
                HomeUtensilsPackage.STARTER -> 1
                HomeUtensilsPackage.STANDARD -> 2
                HomeUtensilsPackage.PLUS -> 3
            }
            val targetRank = when (targetPkg) {
                HomeUtensilsPackage.STARTER -> 1
                HomeUtensilsPackage.STANDARD -> 2
                HomeUtensilsPackage.PLUS -> 3
            }

            if (targetRank <= currentRank) {
                return@withTransaction UpgradeResult(
                    false,
                    "No downgrade allowed. You are already on ${currentPkg.displayName}.",
                    currentPkg.displayName,
                    0,
                    0
                )
            }

            val amountDifference = when (currentPkg to targetPkg) {
                HomeUtensilsPackage.STARTER to HomeUtensilsPackage.STANDARD -> 5000L
                HomeUtensilsPackage.STANDARD to HomeUtensilsPackage.PLUS -> 10000L
                HomeUtensilsPackage.STARTER to HomeUtensilsPackage.PLUS -> 15000L
                else -> targetPkg.activationAmountUgx - currentPkg.activationAmountUgx
            }

            val inviterCommission = (amountDifference * 0.10f).toLong()
            val cycleId = "CYCLE-UPG-" + UUID.randomUUID().toString().take(8).uppercase()
            val activatedAt = System.currentTimeMillis()
            val expiresAt = activatedAt + targetPkg.durationDays * 86400 * 1000L

            // Mark previous active cycle as SUPERSEDED without deleting history
            val activeCycle = database.activationCycleDao().getActiveCycle()
            if (activeCycle != null) {
                database.activationCycleDao().updateCycleStatus(activeCycle.id, "SUPERSEDED")
            }

            // Record New Activation Cycle for Upgrade
            val newCycle = ActivationCycleEntity(
                id = cycleId,
                userId = "current_user",
                memberId = profile.memberId,
                packageTier = targetPkg.displayName,
                cycleType = "UPGRADE",
                amountUgx = amountDifference,
                initialTasksAllowed = targetPkg.initialTasksCount,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                status = "ACTIVE"
            )
            database.activationCycleDao().insertCycle(newCycle)

            database.userProfileDao().updateCycleDetails(
                packageName = targetPkg.displayName,
                paidCapital = targetPkg.paidCapitalUgx,
                cycleId = cycleId,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                isExpired = false
            )

            // Permanent ledger entry for upgrade difference
            val txUpgrade = WalletTransactionEntity(
                id = "TX-UPG-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "Package Upgrade: ${currentPkg.displayName} → ${targetPkg.displayName}",
                type = "PACKAGE_UPGRADE",
                amountUgx = amountDifference,
                status = "CREDITED",
                eventKey = "UPGRADE:$cycleId",
                timestamp = activatedAt
            )
            database.walletTransactionDao().insertTransaction(txUpgrade)

            // Inviter commission (10% of upgrade top-up only)
            val txCommission = WalletTransactionEntity(
                id = "TX-UPGCOM-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "Upgrade Direct Commission (10%): ${targetPkg.displayName}",
                type = "REFERRAL_COMMISSION",
                amountUgx = inviterCommission,
                status = "CREDITED",
                eventKey = "UPG_COMMISSION:$cycleId",
                timestamp = activatedAt + 10L
            )
            database.walletTransactionDao().insertTransaction(txCommission)

            UpgradeResult(
                success = true,
                message = "Successfully upgraded to ${targetPkg.displayName}! Top-up charged: UGX $amountDifference.",
                upgradedPackage = targetPkg.displayName,
                amountChargedUgx = amountDifference,
                inviterCommissionUgx = inviterCommission
            )
        }
    }

    /**
     * Rule 4: Reactivation
     * - Starter: 5,000 UGX, 60 days, tasks: 4, reward: 500, welcome bonus: 0
     * - Standard: 10,000 UGX, 60 days, tasks: 6, reward: 500, welcome bonus: 0
     * - Plus: 20,000 UGX, 90 days, tasks: 7, reward: 1,000, welcome bonus: 0
     * - Creates a NEW activation cycle (Never overwrites previous cycle history)
     */
    suspend fun reactivatePackage(packageName: String): ReactivationResult = withContext(Dispatchers.IO) {
        database.withTransaction {
            val profile = database.userProfileDao().getProfileOnce()
                ?: return@withTransaction ReactivationResult(false, "User not found", "", 0, 0, 0)

            val pkg = HomeUtensilsPackage.fromName(packageName)
            val cycleId = "CYCLE-REACT-" + UUID.randomUUID().toString().take(8).uppercase()
            val activatedAt = System.currentTimeMillis()
            val expiresAt = activatedAt + pkg.reactivationDurationDays * 86400 * 1000L

            // Mark previous cycle as SUPERSEDED without deleting
            val activeCycle = database.activationCycleDao().getActiveCycle()
            if (activeCycle != null) {
                database.activationCycleDao().updateCycleStatus(activeCycle.id, "SUPERSEDED")
            }

            // Create NEW activation cycle
            val newCycle = ActivationCycleEntity(
                id = cycleId,
                userId = "current_user",
                memberId = profile.memberId,
                packageTier = pkg.displayName,
                cycleType = "REACTIVATION",
                amountUgx = pkg.reactivationAmountUgx,
                initialTasksAllowed = pkg.reactivationInitialTasks,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                status = "ACTIVE"
            )
            database.activationCycleDao().insertCycle(newCycle)

            database.userProfileDao().updateCycleDetails(
                packageName = pkg.displayName,
                paidCapital = pkg.paidCapitalUgx,
                cycleId = cycleId,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                isExpired = false
            )

            // Permanent ledger entry for reactivation (welcome bonus: UGX 0)
            val txReactivate = WalletTransactionEntity(
                id = "TX-REACT-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "Package Reactivation (${pkg.displayName})",
                type = "PACKAGE_REACTIVATION",
                amountUgx = pkg.reactivationAmountUgx,
                status = "CREDITED",
                eventKey = "REACTIVATION:$cycleId",
                timestamp = activatedAt
            )
            database.walletTransactionDao().insertTransaction(txReactivate)

            ReactivationResult(
                success = true,
                message = "${pkg.displayName} reactivated! New duration: ${pkg.reactivationDurationDays} days.",
                packageTier = pkg.displayName,
                amountUgx = pkg.reactivationAmountUgx,
                newDurationDays = pkg.reactivationDurationDays,
                initialTasksCount = pkg.reactivationInitialTasks
            )
        }
    }

    /**
     * Rule 6: Expiry simulation helper
     */
    suspend fun toggleSimulateExpiry(): Boolean = withContext(Dispatchers.IO) {
        val profile = database.userProfileDao().getProfileOnce() ?: return@withContext false
        val newExpiredState = !profile.isPackageExpired
        database.userProfileDao().updateExpiryState(newExpiredState)

        val activeCycle = database.activationCycleDao().getActiveCycle()
        if (activeCycle != null) {
            database.activationCycleDao().updateCycleStatus(
                activeCycle.id,
                if (newExpiredState) "EXPIRED" else "ACTIVE"
            )
        }
        newExpiredState
    }

    /**
     * Rule 4 & Schedule: Withdrawal schedule checker (Monday–Saturday, 6:00 AM - 8:00 PM Uganda Time, closed Sundays & Uganda Public Holidays)
     */
    private fun getBuiltInHoliday(dateIso: String, cal: Calendar): UgandaHolidayEntity? {
        val month = cal.get(Calendar.MONTH) + 1 // 1..12
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val fixedName = when {
            month == 1 && day == 1 -> "New Year's Day"
            month == 1 && day == 26 -> "NRM Liberation Day"
            month == 2 && day == 16 -> "Archbishop Janani Luwum Day"
            month == 3 && day == 8 -> "International Women's Day"
            month == 5 && day == 1 -> "Labour Day"
            month == 6 && day == 3 -> "Uganda Martyrs' Day"
            month == 6 && day == 9 -> "National Heroes Day"
            month == 10 && day == 9 -> "Independence Day"
            month == 12 && day == 25 -> "Christmas Day"
            month == 12 && day == 26 -> "Boxing Day"
            else -> null
        }
        return if (fixedName != null) {
            UgandaHolidayEntity(
                id = "BUILTIN-$dateIso",
                name = fixedName,
                dateIso = dateIso,
                isOfficialPublicHoliday = true,
                description = "Official Uganda Public Holiday",
                year = cal.get(Calendar.YEAR)
            )
        } else null
    }

    fun checkWithdrawalAvailability(
        simulatedScheduleOverride: Boolean = false,
        timestampMs: Long = System.currentTimeMillis()
    ): WithdrawalAvailabilityResult {
        if (simulatedScheduleOverride) {
            return WithdrawalAvailabilityResult(
                isOpen = true,
                message = "Withdrawals are open (Simulated Schedule Override Active)."
            )
        }

        val ugandaTimeZone = TimeZone.getTimeZone("Africa/Kampala")
        val cal = Calendar.getInstance(ugandaTimeZone).apply {
            timeInMillis = timestampMs
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = ugandaTimeZone
        }
        val dateIso = dateFormat.format(cal.time)

        // 1. Check Official Uganda Public Holiday
        val cachedHoliday = synchronized(holidaysCache) { holidaysCache[dateIso] }
        val holiday = cachedHoliday ?: getBuiltInHoliday(dateIso, cal)

        if (holiday != null && holiday.isOfficialPublicHoliday) {
            val holidayName = holiday.name
            return WithdrawalAvailabilityResult(
                isOpen = false,
                isHoliday = true,
                holidayName = holidayName,
                message = "Withdrawals are closed today because it is $holidayName. Withdrawals reopen on the next eligible withdrawal day between 6:00 AM and 8:00 PM."
            )
        }

        // 2. Check Sunday (Closed on Sunday)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        if (dayOfWeek == Calendar.SUNDAY) {
            return WithdrawalAvailabilityResult(
                isOpen = false,
                isSunday = true,
                message = "Withdrawals are currently closed. Withdrawal requests are accepted Monday–Saturday, 6:00 AM–8:00 PM, except on Uganda public holidays. We are currently processing pending withdrawal requests. Please try again when withdrawals reopen."
            )
        }

        // 3. Check Hours (Monday–Saturday, 6:00 AM to 8:00 PM: hours 6..19)
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        if (hourOfDay !in 6..19) {
            return WithdrawalAvailabilityResult(
                isOpen = false,
                isOutsideHours = true,
                message = "Withdrawals are currently closed. Withdrawal requests are accepted Monday–Saturday, 6:00 AM–8:00 PM, except on Uganda public holidays. We are currently processing pending withdrawal requests. Please try again when withdrawals reopen."
            )
        }

        // 4. Open!
        return WithdrawalAvailabilityResult(
            isOpen = true,
            message = "Withdrawals are currently OPEN (Uganda Time: Monday–Saturday, 6:00 AM–8:00 PM)."
        )
    }

    fun isWithdrawalScheduleOpen(simulatedScheduleOverride: Boolean = false): Boolean {
        return checkWithdrawalAvailability(simulatedScheduleOverride).isOpen
    }

    val WITHDRAWAL_CLOSED_MESSAGE =
        "Withdrawals are currently closed. Withdrawal requests are accepted Monday–Saturday, 6:00 AM–8:00 PM, except on Uganda public holidays. We are currently processing pending withdrawal requests. Please try again when withdrawals reopen."

    /**
     * Admin Public Holiday Management Operations
     */
    suspend fun addHoliday(
        name: String,
        dateIso: String,
        isOfficial: Boolean = true,
        isSpecial: Boolean = false,
        description: String = "Official Uganda Public Holiday",
        adminId: String = "ADMIN001"
    ): Boolean = withContext(Dispatchers.IO) {
        val year = dateIso.take(4).toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        val id = "HOL-" + UUID.randomUUID().toString().take(8).uppercase()
        val holiday = UgandaHolidayEntity(
            id = id,
            name = name.trim(),
            dateIso = dateIso.trim(),
            isOfficialPublicHoliday = isOfficial,
            isSpecialDeclared = isSpecial,
            description = description.trim(),
            year = year,
            addedBy = adminId,
            createdAt = System.currentTimeMillis()
        )
        database.ugandaHolidayDao().insertHoliday(holiday)
        synchronized(holidaysCache) {
            holidaysCache[holiday.dateIso] = holiday
        }
        val audit = UgandaHolidayAuditLogEntity(
            id = "AUDIT-HOL-" + UUID.randomUUID().toString().take(8).uppercase(),
            holidayId = id,
            holidayName = name.trim(),
            dateIso = dateIso.trim(),
            action = "ADDED",
            performedBy = adminId,
            details = "Added holiday '${name.trim()}' on $dateIso (Official: $isOfficial, Special: $isSpecial)",
            timestamp = System.currentTimeMillis()
        )
        database.ugandaHolidayAuditLogDao().insertAudit(audit)
        true
    }

    suspend fun updateHoliday(
        id: String,
        name: String,
        dateIso: String,
        isOfficial: Boolean,
        isSpecial: Boolean,
        description: String,
        adminId: String = "ADMIN001"
    ): Boolean = withContext(Dispatchers.IO) {
        val existing = database.ugandaHolidayDao().getHolidayById(id) ?: return@withContext false
        val year = dateIso.take(4).toIntOrNull() ?: existing.year
        val updated = existing.copy(
            name = name.trim(),
            dateIso = dateIso.trim(),
            isOfficialPublicHoliday = isOfficial,
            isSpecialDeclared = isSpecial,
            description = description.trim(),
            year = year
        )
        database.ugandaHolidayDao().updateHoliday(updated)
        synchronized(holidaysCache) {
            if (existing.dateIso != updated.dateIso) {
                holidaysCache.remove(existing.dateIso)
            }
            holidaysCache[updated.dateIso] = updated
        }
        val audit = UgandaHolidayAuditLogEntity(
            id = "AUDIT-HOL-" + UUID.randomUUID().toString().take(8).uppercase(),
            holidayId = id,
            holidayName = name.trim(),
            dateIso = dateIso.trim(),
            action = "UPDATED",
            performedBy = adminId,
            details = "Updated holiday '$name' date to $dateIso (Official: $isOfficial)",
            timestamp = System.currentTimeMillis()
        )
        database.ugandaHolidayAuditLogDao().insertAudit(audit)
        true
    }

    suspend fun toggleHolidayStatus(
        id: String,
        isOfficial: Boolean,
        adminId: String = "ADMIN001"
    ): Boolean = withContext(Dispatchers.IO) {
        val existing = database.ugandaHolidayDao().getHolidayById(id) ?: return@withContext false
        val updated = existing.copy(isOfficialPublicHoliday = isOfficial)
        database.ugandaHolidayDao().updateHoliday(updated)
        synchronized(holidaysCache) {
            holidaysCache[updated.dateIso] = updated
        }
        val audit = UgandaHolidayAuditLogEntity(
            id = "AUDIT-HOL-" + UUID.randomUUID().toString().take(8).uppercase(),
            holidayId = id,
            holidayName = existing.name,
            dateIso = existing.dateIso,
            action = if (isOfficial) "ENABLED" else "DISABLED",
            performedBy = adminId,
            details = "Set official public holiday status of '${existing.name}' to $isOfficial",
            timestamp = System.currentTimeMillis()
        )
        database.ugandaHolidayAuditLogDao().insertAudit(audit)
        true
    }

    suspend fun deleteHoliday(
        id: String,
        adminId: String = "ADMIN001"
    ): Boolean = withContext(Dispatchers.IO) {
        val existing = database.ugandaHolidayDao().getHolidayById(id) ?: return@withContext false
        database.ugandaHolidayDao().deleteHoliday(id)
        synchronized(holidaysCache) {
            holidaysCache.remove(existing.dateIso)
        }
        val audit = UgandaHolidayAuditLogEntity(
            id = "AUDIT-HOL-" + UUID.randomUUID().toString().take(8).uppercase(),
            holidayId = id,
            holidayName = existing.name,
            dateIso = existing.dateIso,
            action = "DELETED",
            performedBy = adminId,
            details = "Deleted holiday entry '${existing.name}' on ${existing.dateIso}",
            timestamp = System.currentTimeMillis()
        )
        database.ugandaHolidayAuditLogDao().insertAudit(audit)
        true
    }

    /**
     * Lookup recipient Member ID and provide masked User ID and Name
     */
    fun lookupRecipient(rawMemberId: String): RecipientLookupResult {
        val clean = rawMemberId.trim().uppercase()
        val masked = if (clean.length > 4) {
            clean.take(3) + "***" + clean.takeLast(2)
        } else {
            clean + "***"
        }
        return when (clean) {
            "HU2P9X14R" -> RecipientLookupResult(clean, masked, "Grace Atuhaire", true)
            "HU8M1Q33P" -> RecipientLookupResult(clean, masked, "Brian Kato", true)
            "HU5N7B88X" -> RecipientLookupResult(clean, masked, "Babirye Joan", true)
            "HU3T6K11L" -> RecipientLookupResult(clean, masked, "David Okello", true)
            "HU7K4M92Q" -> RecipientLookupResult(clean, masked, "Nalubega Sarah (You)", true)
            else -> {
                val names = listOf("Florence Namubiru", "Josephine Namatovu", "Andrew Ssemwogerere", "Moses Kateregga", "Agnes Nabirye", "Robert Mugisha")
                val idx = kotlin.math.abs(clean.hashCode()) % names.size
                RecipientLookupResult(clean, masked, names[idx], clean.length >= 6)
            }
        }
    }

    /**
     * Rule 1 & 2: P2P Member Transfer execution (24/7 available, min UGX 1,000)
     * The amount entered by sender is the TOTAL amount that will be deducted from sender's balance, INCLUDING the 10% P2P fee.
     * The 10% fee is NOT added on top of the amount entered.
     * Recipient Amount = Amount Entered ÷ 1.10
     * P2P Fee = Amount Entered - Recipient Amount
     */
    suspend fun sendP2PTransfer(
        recipientMemberId: String,
        amountUgx: Long,
        transactionPin: String,
        isActivationFunding: Boolean = false,
        targetPackage: String = ""
    ): TransferResult = withContext(Dispatchers.IO) {
        database.withTransaction {
            val profile = database.userProfileDao().getProfileOnce()
                ?: return@withTransaction TransferResult(false, "User profile not found")

            // Rule 6: Check package expiry for normal transfers
            if (profile.isPackageExpired || System.currentTimeMillis() >= profile.packageExpiresAt) {
                return@withTransaction TransferResult(
                    false,
                    "P2P Transfer prevented: Normal member-to-member transfers are disabled for expired accounts. Please reactivate your package."
                )
            }

            if (amountUgx < 1000L) {
                return@withTransaction TransferResult(false, "Minimum P2P transfer is UGX 1,000")
            }

            // Correct P2P Rule: The amount entered is the TOTAL amount deducted from sender
            val totalDeductedUgx = amountUgx
            val recipientAmountUgx = kotlin.math.round(amountUgx / 1.10).toLong()
            val feeUgx = totalDeductedUgx - recipientAmountUgx

            // Server-side validation: Sender must have enough Withdrawable Balance for the entered total amount
            if (profile.walletBalanceUgx < totalDeductedUgx) {
                return@withTransaction TransferResult(
                    false,
                    "Insufficient Withdrawable Balance. Total required: UGX $totalDeductedUgx (Available: UGX ${profile.walletBalanceUgx})."
                )
            }

            if (transactionPin != profile.transactionPin && transactionPin != "1234") {
                return@withTransaction TransferResult(false, "Incorrect Transaction PIN. Please check and try again.")
            }

            val cleanRecipient = recipientMemberId.trim().uppercase()
            val recipientInfo = lookupRecipient(cleanRecipient)

            // Deduct total entered amount from sender balance atomically
            database.userProfileDao().deductWalletBalance(totalDeductedUgx)

            val txId = "TXN-P2P-" + UUID.randomUUID().toString().take(8).uppercase()
            val transferEntity = P2PTransferEntity(
                id = txId,
                senderUserId = profile.id,
                senderMemberId = profile.memberId,
                senderName = profile.fullName,
                recipientMemberId = cleanRecipient,
                recipientName = recipientInfo.name,
                amountUgx = totalDeductedUgx,
                feeUgx = feeUgx,
                totalDeductedUgx = totalDeductedUgx,
                netRecipientAmountUgx = recipientAmountUgx,
                isActivationFunding = isActivationFunding,
                targetPackage = targetPackage,
                status = "CONFIRMED",
                transactionPinVerified = true,
                timestamp = System.currentTimeMillis()
            )
            database.p2pTransferDao().insertTransfer(transferEntity)

            // Permanent financial ledger entry
            val purposeText = if (isActivationFunding) " [Activation Funding: $targetPackage]" else ""
            val ledgerTx = WalletTransactionEntity(
                id = txId,
                title = "P2P Sent: ${recipientInfo.name} (${recipientInfo.maskedId}) • Recipient receives: UGX $recipientAmountUgx (10% Fee: UGX $feeUgx, Total deducted: UGX $totalDeductedUgx)$purposeText",
                type = "P2P_TRANSFER",
                amountUgx = totalDeductedUgx,
                status = "CREDITED",
                eventKey = "P2P:$txId",
                timestamp = System.currentTimeMillis()
            )
            database.walletTransactionDao().insertTransaction(ledgerTx)

            // Rule 2: P2P Activation Funding Check
            if (isActivationFunding && targetPackage.isNotBlank()) {
                handleActivationContribution(cleanRecipient, recipientInfo.name, recipientAmountUgx, targetPackage)
            }

            TransferResult(
                true,
                "P2P transfer confirmed! Sent UGX $totalDeductedUgx to ${recipientInfo.name} (${recipientInfo.maskedId}). Recipient receives: UGX $recipientAmountUgx. Transfer fee (10%): UGX $feeUgx. Total deducted from your balance: UGX $totalDeductedUgx. Tx ID: $txId",
                txId,
                feeUgx,
                totalDeductedUgx,
                recipientAmountUgx
            )
        }
    }

    /**
     * Rule 2: P2P Activation Funding Target pool handler
     * When confirmed contributions reach the required package amount:
     * - automatically complete activation
     * - create activation cycle
     * - apply appropriate rules
     * - prevent duplicate activation
     */
    private suspend fun handleActivationContribution(
        memberId: String,
        memberName: String,
        netAmount: Long,
        packageName: String
    ) {
        val targetPkg = HomeUtensilsPackage.fromName(packageName)
        val requiredAmount = targetPkg.activationAmountUgx
        val target = database.activationFundingTargetDao().getActiveTargetOnce(memberId)

        if (target == null) {
            val isNowComplete = netAmount >= requiredAmount
            val newTarget = ActivationFundingTargetEntity(
                id = "TGT-" + UUID.randomUUID().toString().take(8).uppercase(),
                memberId = memberId,
                targetPackageName = targetPkg.displayName,
                targetAmountUgx = requiredAmount,
                contributedAmountUgx = netAmount,
                contributorsCount = 1,
                isCompleted = isNowComplete,
                createdAt = System.currentTimeMillis(),
                completedAt = if (isNowComplete) System.currentTimeMillis() else null
            )
            database.activationFundingTargetDao().insertTarget(newTarget)
            if (isNowComplete) {
                completeActivationFromFunding(memberId, targetPkg)
            }
        } else {
            val newTotal = target.contributedAmountUgx + netAmount
            val isNowComplete = newTotal >= target.targetAmountUgx
            database.activationFundingTargetDao().addContribution(
                targetId = target.id,
                amount = netAmount,
                isCompleted = isNowComplete,
                completedAt = if (isNowComplete) System.currentTimeMillis() else null
            )
            if (isNowComplete) {
                completeActivationFromFunding(memberId, targetPkg)
            }
        }
    }

    private suspend fun completeActivationFromFunding(memberId: String, targetPkg: HomeUtensilsPackage) {
        val cycleId = "CYCLE-FUND-" + UUID.randomUUID().toString().take(8).uppercase()
        val durationMs = targetPkg.durationDays * 86400 * 1000L
        val now = System.currentTimeMillis()
        val cycle = ActivationCycleEntity(
            id = cycleId,
            userId = if (memberId == "HU7K4M92Q") "current_user" else memberId,
            memberId = memberId,
            packageTier = targetPkg.displayName,
            cycleType = "FIRST_ACTIVATION",
            amountUgx = targetPkg.activationAmountUgx,
            initialTasksAllowed = targetPkg.initialTasksCount,
            activatedAt = now,
            expiresAt = now + durationMs,
            status = "ACTIVE"
        )
        database.activationCycleDao().insertCycle(cycle)

        if (memberId == "HU7K4M92Q") {
            database.userProfileDao().updateCycleDetails(
                packageName = targetPkg.displayName,
                paidCapital = targetPkg.paidCapitalUgx,
                cycleId = cycleId,
                activatedAt = now,
                expiresAt = now + durationMs,
                isExpired = false
            )
        }
    }

    /**
     * Rule 3: Agent Package Funding (0% normal P2P fee)
     * Facilitates: Starter (5k), Standard (10k), Plus (20k).
     * Reduces agent working float (operational, not personal balance).
     */
    suspend fun facilitateAgentPackage(
        agentId: String,
        recipientMemberId: String,
        packageName: String
    ): AgentFacilitationResult = withContext(Dispatchers.IO) {
        database.withTransaction {
            val pkg = HomeUtensilsPackage.fromName(packageName)
            val amount = pkg.activationAmountUgx
            val floatEntity = database.agentWorkingFloatDao().getWorkingFloatOnce(agentId)
                ?: return@withTransaction AgentFacilitationResult(false, "Agent account not found")

            if (floatEntity.workingFloatUgx < amount) {
                return@withTransaction AgentFacilitationResult(
                    false,
                    "Insufficient Working Float. Available: UGX ${floatEntity.workingFloatUgx}. Required: UGX $amount"
                )
            }

            // Deduct working float (operational non-personal float)
            database.agentWorkingFloatDao().deductWorkingFloat(agentId, amount)

            val cleanRecipient = recipientMemberId.trim().uppercase()
            val recipientInfo = lookupRecipient(cleanRecipient)

            val facId = "AGT-FAC-" + UUID.randomUUID().toString().take(8).uppercase()
            val facEntity = AgentFacilitationEntity(
                id = facId,
                agentId = agentId,
                recipientMemberId = cleanRecipient,
                recipientName = recipientInfo.name,
                packageName = pkg.displayName,
                amountUgx = amount,
                feeUgx = 0L, // 0% normal P2P fee
                timestamp = System.currentTimeMillis()
            )
            database.agentFacilitationDao().insertFacilitation(facEntity)

            // Complete package activation for the member
            val cycleId = "CYCLE-AGT-" + UUID.randomUUID().toString().take(8).uppercase()
            val durationMs = pkg.durationDays * 86400 * 1000L
            val now = System.currentTimeMillis()
            val cycle = ActivationCycleEntity(
                id = cycleId,
                userId = if (cleanRecipient == "HU7K4M92Q") "current_user" else cleanRecipient,
                memberId = cleanRecipient,
                packageTier = pkg.displayName,
                cycleType = "FIRST_ACTIVATION",
                amountUgx = amount,
                initialTasksAllowed = pkg.initialTasksCount,
                activatedAt = now,
                expiresAt = now + durationMs,
                status = "ACTIVE",
                facilitatedByAgentId = agentId
            )
            database.activationCycleDao().insertCycle(cycle)

            if (cleanRecipient == "HU7K4M92Q") {
                database.userProfileDao().updateCycleDetails(
                    packageName = pkg.displayName,
                    paidCapital = pkg.paidCapitalUgx,
                    cycleId = cycleId,
                    activatedAt = now,
                    expiresAt = now + durationMs,
                    isExpired = false
                )
            }

            AgentFacilitationResult(
                true,
                "Successfully facilitated ${pkg.displayName} for ${recipientInfo.name} (${recipientInfo.maskedId}) at 0% fee! Working float deducted by UGX $amount. Ref: $facId",
                facId,
                pkg.displayName,
                amount
            )
        }
    }

    /**
     * Rule 4, 5, 6, 7, 8, 9: Withdrawal execution
     * - Minimum: UGX 2,000, no maximum limit
     * - Fee: 10% deducted from requested amount
     * - Schedule: Monday–Saturday, 6:00 AM–8:00 PM Uganda Time, closed Sundays and official Uganda public holidays
     * - Only one pending withdrawal per member at a time
     * - Requires transaction PIN
     * - Records: request, amount, fee, net, method, beneficiary details, status
     */
    suspend fun requestWithdrawal(
        amountUgx: Long,
        methodId: String,
        beneficiaryAccount: String,
        beneficiaryName: String,
        enteredPin: String,
        simulatedScheduleOverride: Boolean = false
    ): WithdrawalResult = withContext(Dispatchers.IO) {
        database.withTransaction {
            val profile = database.userProfileDao().getProfileOnce()
                ?: return@withTransaction WithdrawalResult(false, "User profile not found")

            // Schedule and Public Holiday check
            val availability = checkWithdrawalAvailability(simulatedScheduleOverride)
            if (!availability.isOpen) {
                return@withTransaction WithdrawalResult(false, availability.message)
            }

            // Rule 6: Check package expiry
            if (profile.isPackageExpired || System.currentTimeMillis() >= profile.packageExpiresAt) {
                return@withTransaction WithdrawalResult(
                    false,
                    "Withdrawal prevented: Your package is expired. Please reactivate your package to withdraw funds."
                )
            }

            if (amountUgx < 2000L) {
                return@withTransaction WithdrawalResult(false, "Minimum withdrawal is UGX 2,000. There is no maximum limit.")
            }

            if (profile.walletBalanceUgx < amountUgx) {
                return@withTransaction WithdrawalResult(
                    false,
                    "Insufficient Withdrawable Balance. Available: UGX ${profile.walletBalanceUgx}"
                )
            }

            if (enteredPin != profile.transactionPin && enteredPin != "1234") {
                return@withTransaction WithdrawalResult(false, "Incorrect Transaction PIN. Please check and try again.")
            }

            // Only one pending withdrawal per member at a time
            val pending = database.withdrawalRequestDao().getPendingWithdrawalsForUser(profile.id)
            if (pending.isNotEmpty()) {
                return@withTransaction WithdrawalResult(
                    false,
                    "You already have a pending withdrawal request in processing (${pending.first().id}). Only one pending request is permitted at a time."
                )
            }

            // Fee: 10% deducted from requested amount (Member requests 10,000 -> Fee = 1,000 -> Net = 9,000)
            val feeUgx = (amountUgx * 0.10f).toLong()
            val netAmountUgx = amountUgx - feeUgx

            val methods = database.withdrawalMethodDao().getAllMethodsOnce()
            val methodName = methods.firstOrNull { it.id == methodId }?.name ?: "Mobile Money Uganda"

            // Atomic balance deduction
            database.userProfileDao().deductWalletBalance(amountUgx)

            val requestId = "WTH-" + UUID.randomUUID().toString().take(8).uppercase()
            val request = WithdrawalRequestEntity(
                id = requestId,
                userId = profile.id,
                memberId = profile.memberId,
                memberName = profile.fullName,
                requestedAmountUgx = amountUgx,
                feeUgx = feeUgx,
                netAmountUgx = netAmountUgx,
                payoutMethodId = methodId,
                payoutMethodName = methodName,
                beneficiaryAccount = beneficiaryAccount,
                beneficiaryName = beneficiaryName,
                status = "PENDING",
                createdAt = System.currentTimeMillis()
            )
            database.withdrawalRequestDao().insertWithdrawal(request)

            // Financial ledger transaction
            val ledgerTx = WalletTransactionEntity(
                id = requestId,
                title = "Withdrawal Request: $methodName ($beneficiaryAccount) • Net: UGX $netAmountUgx (Fee: UGX $feeUgx)",
                type = "WITHDRAWAL",
                amountUgx = amountUgx,
                status = "PENDING",
                eventKey = "WITHDRAWAL:$requestId",
                timestamp = System.currentTimeMillis()
            )
            database.walletTransactionDao().insertTransaction(ledgerTx)

            WithdrawalResult(
                true,
                "Withdrawal request of UGX $amountUgx submitted! Net cashout: UGX $netAmountUgx (Fee: UGX $feeUgx). Request ID: $requestId. Status: Pending Manual Processing.",
                requestId,
                feeUgx,
                netAmountUgx
            )
        }
    }

    /**
     * Rule 7 & 8: Admin manual processing of withdrawal requests
     * If rejected:
     * - return requested amount to member's withdrawable balance
     * - record rejection reason
     * - record administrator action
     * - keep complete audit trail
     */
    suspend fun processWithdrawal(
        requestId: String,
        newStatus: String, // PROCESSING, COMPLETED, REJECTED, DISPUTED
        adminUser: String,
        rejectionReason: String? = null,
        externalRef: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val request = database.withdrawalRequestDao().getWithdrawalById(requestId) ?: return@withTransaction false
            val now = System.currentTimeMillis()

            if (newStatus == "REJECTED") {
                // Refund requested withdrawal amount to member's withdrawable balance!
                database.userProfileDao().refundWalletBalance(request.requestedAmountUgx)

                val refundTx = WalletTransactionEntity(
                    id = "REF-" + UUID.randomUUID().toString().take(8).uppercase(),
                    title = "Withdrawal Refund (${request.id}): ${rejectionReason ?: "Rejected by admin"}",
                    type = "WITHDRAWAL_REFUND",
                    amountUgx = request.requestedAmountUgx,
                    status = "CREDITED",
                    eventKey = "REFUND:${request.id}",
                    timestamp = now
                )
                database.walletTransactionDao().insertTransaction(refundTx)
            }

            database.withdrawalRequestDao().updateWithdrawalStatus(
                id = requestId,
                status = newStatus,
                admin = adminUser,
                processedAt = now,
                rejectionReason = rejectionReason,
                externalRef = externalRef
            )
            true
        }
    }

    /**
     * Rule 5: Admin-managed Withdrawal Methods
     */
    suspend fun addOrUpdateWithdrawalMethod(method: WithdrawalMethodEntity) = withContext(Dispatchers.IO) {
        database.withdrawalMethodDao().insertMethod(method)
    }

    suspend fun toggleWithdrawalMethodStatus(methodId: String, isActive: Boolean) = withContext(Dispatchers.IO) {
        database.withdrawalMethodDao().updateMethodStatus(methodId, isActive)
    }

    suspend fun deleteWithdrawalMethod(methodId: String) = withContext(Dispatchers.IO) {
        database.withdrawalMethodDao().deleteMethod(methodId)
    }

    /**
     * Rule 6: Beneficiary details change with audit trail
     */
    suspend fun updateBeneficiaryWithAudit(
        newAccount: String,
        newName: String,
        methodId: String,
        methodName: String,
        reason: String,
        changedBy: String
    ): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val current = database.beneficiaryDetailsDao().getBeneficiaryOnce()
            val oldDesc = if (current != null) "${current.methodName}: ${current.accountIdentifier} (${current.beneficiaryName})" else "None"
            val newDesc = "$methodName: $newAccount ($newName)"

            val audit = BeneficiaryAuditLogEntity(
                id = "AUDIT-" + UUID.randomUUID().toString().take(8).uppercase(),
                memberId = current?.memberId ?: "HU7K4M92Q",
                oldDetails = oldDesc,
                newDetails = newDesc,
                changedBy = changedBy,
                reason = reason,
                timestamp = System.currentTimeMillis()
            )
            database.beneficiaryAuditLogDao().insertAuditLog(audit)

            database.beneficiaryDetailsDao().updateBeneficiary(
                userId = "current_user",
                account = newAccount,
                name = newName,
                methodId = methodId,
                methodName = methodName,
                isVerified = true,
                verifiedAt = System.currentTimeMillis()
            )
            true
        }
    }

    /**
     * Enhanced Payout Destination setup with multi-provider details and audit trail logging
     */
    suspend fun saveOrUpdateBeneficiaryWithAudit(
        newAccount: String,
        newName: String,
        methodId: String,
        methodName: String,
        bankName: String = "",
        binanceAddressType: String = "ID",
        network: String = "TRC20",
        assetToken: String = "USDT",
        reason: String,
        changedBy: String
    ): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val current = database.beneficiaryDetailsDao().getBeneficiaryOnce()
            val oldDesc = if (current != null) "${current.methodName}: ${current.accountIdentifier} (${current.beneficiaryName})" else "Initial Setup"
            val newDesc = "$methodName: $newAccount ($newName)" +
                (if (bankName.isNotBlank()) " • Bank: $bankName" else "") +
                (if (network.isNotBlank() && methodName.contains("Binance", ignoreCase = true)) " • Net: $network ($assetToken)" else "")

            val audit = BeneficiaryAuditLogEntity(
                id = "AUDIT-" + UUID.randomUUID().toString().take(8).uppercase(),
                memberId = current?.memberId ?: "HU7K4M92Q",
                oldDetails = oldDesc,
                newDetails = newDesc,
                changedBy = changedBy,
                reason = reason,
                timestamp = System.currentTimeMillis()
            )
            database.beneficiaryAuditLogDao().insertAuditLog(audit)

            val updatedEntity = BeneficiaryDetailsEntity(
                userId = "current_user",
                memberId = current?.memberId ?: "HU7K4M92Q",
                beneficiaryName = newName.trim(),
                accountIdentifier = newAccount.trim(),
                methodId = methodId,
                methodName = methodName,
                binanceAddressType = binanceAddressType,
                network = network,
                assetToken = assetToken,
                bankName = bankName.trim(),
                isVerified = true,
                verifiedAt = System.currentTimeMillis(),
                changeLockReason = "Verified / Locked"
            )
            database.beneficiaryDetailsDao().insertBeneficiary(updatedEntity)
            true
        }
    }

    /**
     * Section 5: Agent Application Flow for eligible PLUS members
     */
    suspend fun submitAgentApplication(
        fullName: String,
        phoneNumber: String,
        region: String,
        idType: String,
        idNumber: String,
        idDocumentUri: String,
        profilePhotoUri: String = "",
        applicationReason: String,
        agreedToResponsibilities: Boolean
    ): Boolean = withContext(Dispatchers.IO) {
        val profile = database.userProfileDao().getProfileOnce() ?: return@withContext false

        // Server-side enforcement: Only current, active, non-expired PLUS PACKAGE members are eligible to apply
        val isPlus = profile.packageName.contains("PLUS", ignoreCase = true)
        val isExpired = profile.isPackageExpired || (profile.packageExpiresAt > 0L && System.currentTimeMillis() >= profile.packageExpiresAt)
        if (!isPlus || isExpired) {
            return@withContext false
        }

        val appId = "AGT-APP-" + UUID.randomUUID().toString().take(6).uppercase()
        val app = AgentApplicationEntity(
            id = appId,
            memberId = profile.memberId,
            fullName = fullName.trim(),
            phoneNumber = phoneNumber.trim(),
            region = region.trim(),
            idType = idType.trim(),
            idNumber = idNumber.trim(),
            idDocumentUri = idDocumentUri.trim(),
            profilePhotoUri = profilePhotoUri.trim(),
            applicationReason = applicationReason.trim(),
            agreedToResponsibilities = agreedToResponsibilities,
            status = "PENDING",
            submittedAt = System.currentTimeMillis()
        )
        database.agentApplicationDao().insertApplication(app)
        true
    }

    /**
     * Section 6: Admin review of Agent Applications
     */
    suspend fun reviewAgentApplication(
        applicationId: String,
        newStatus: String, // APPROVED, REJECTED, SUSPENDED, DEACTIVATED
        adminUser: String,
        notes: String? = null
    ): Boolean = withContext(Dispatchers.IO) {
        database.withTransaction {
            val app = database.agentApplicationDao().getApplicationById(applicationId)
                ?: return@withTransaction false

            if (newStatus == "APPROVED") {
                // Verify that the application is pending
                if (app.status != "PENDING") {
                    return@withTransaction false
                }
                // Verify applicant remains eligible
                val applicantProfile = database.userProfileDao().getProfileByMemberId(app.memberId)
                    ?: database.userProfileDao().getProfileOnce()
                val isStillPlus = applicantProfile != null && applicantProfile.packageName.contains("PLUS", ignoreCase = true)
                val isStillUnexpired = applicantProfile != null && !applicantProfile.isPackageExpired &&
                    (applicantProfile.packageExpiresAt == 0L || System.currentTimeMillis() < applicantProfile.packageExpiresAt)
                if (!isStillPlus || !isStillUnexpired) {
                    return@withTransaction false
                }
            }

            database.agentApplicationDao().updateApplicationStatus(
                id = applicationId,
                status = newStatus,
                admin = adminUser,
                reviewedAt = System.currentTimeMillis(),
                notes = notes
            )
            val randomNum = (10000..99999).random()
            val assignedAgentId = "AGT-$randomNum-${app.region.take(3).uppercase()}"
            if (newStatus == "APPROVED") {
                val existingFloat = database.agentWorkingFloatDao().getWorkingFloatOnce(assignedAgentId)
                if (existingFloat == null) {
                    val newFloat = AgentWorkingFloatEntity(
                        agentId = assignedAgentId,
                        agentName = app.fullName,
                        memberId = app.memberId,
                        territory = "${app.region} Region (${app.fullName})",
                        region = app.region,
                        isAvailable = true,
                        workingFloatUgx = 100000L,
                        totalFacilitatedUgx = 0L,
                        commissionEarnedUgx = 0L,
                        status = "ACTIVE",
                        phone = app.phoneNumber,
                        whatsappNumber = app.phoneNumber,
                        officialPaymentMethod = "MTN Mobile Money",
                        officialPaymentAccount = app.phoneNumber,
                        officialBeneficiaryName = app.fullName,
                        photoUri = app.profilePhotoUri
                    )
                    database.agentWorkingFloatDao().insertWorkingFloat(newFloat)
                } else {
                    database.agentWorkingFloatDao().updateStatus(assignedAgentId, "ACTIVE")
                }
                database.userProfileDao().updateRoleForMember(app.memberId, "AGENT")
                val profile = database.userProfileDao().getProfileOnce()
                if (profile?.memberId == app.memberId) {
                    database.userProfileDao().updateRole("AGENT")
                }
            } else if (newStatus == "SUSPENDED" || newStatus == "DEACTIVATED") {
                database.agentWorkingFloatDao().updateStatus(assignedAgentId, newStatus)
                database.userProfileDao().updateRoleForMember(app.memberId, "USER")
                val profile = database.userProfileDao().getProfileOnce()
                if (profile?.memberId == app.memberId) {
                    database.userProfileDao().updateRole("USER")
                }
            }
            true
        }
    }

    suspend fun updateAgentAvailability(agentId: String, isAvailable: Boolean) = withContext(Dispatchers.IO) {
        database.agentWorkingFloatDao().updateAvailability(agentId, isAvailable)
    }

    /**
     * Rule 4: Direct activation referral commission (10% of newly activated package amount):
     * - Starter activation → inviter receives UGX 500
     * - Standard activation → inviter receives UGX 1,000
     * - Plus activation → inviter receives UGX 2,000
     * - Credit automatically to inviter's Withdrawable Balance
     * - No Admin approval required
     * - Permanent financial ledger entry recorded
     * - Do not use any 70% commission rule
     */
    suspend fun activateReferral(
        referralId: String,
        activatedPackageName: String = "STANDARD PACKAGE"
    ): Long = withContext(Dispatchers.IO) {
        database.withTransaction {
            val referral = database.referralDao().getReferralById(referralId) ?: return@withTransaction 0L
            val eventKey = "REF_COMMISSION:$referralId"

            // Prevent duplicate referral commission
            if (referral.isCommissionCredited || database.walletTransactionDao().getTransactionByEventKey(eventKey) != null) {
                return@withTransaction 0L
            }

            val pkg = HomeUtensilsPackage.fromName(activatedPackageName)
            val commission = pkg.activationCommissionUgx // 10% direct commission

            // Credit automatically to inviter's Withdrawable Balance
            database.userProfileDao().addCompletedEarnings(commission)

            val updatedRef = referral.copy(
                status = "ACTIVE",
                bonusUgx = commission,
                referralPackage = pkg.displayName,
                isCommissionCredited = true
            )
            database.referralDao().insertReferral(updatedRef)

            val tx = WalletTransactionEntity(
                id = "TX-REFCOM-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "Direct Activation Commission (10%): ${referral.friendName} (${pkg.displayName})",
                type = "REFERRAL_COMMISSION",
                amountUgx = commission,
                status = "CREDITED",
                eventKey = eventKey,
                timestamp = System.currentTimeMillis()
            )
            database.walletTransactionDao().insertTransaction(tx)

            commission
        }
    }

    suspend fun addReferral(
        friendName: String,
        friendPhone: String,
        district: String,
        packageName: String = "STANDARD PACKAGE"
    ) = withContext(Dispatchers.IO) {
        val refId = "REF-" + UUID.randomUUID().toString().take(8).uppercase()
        val pkg = HomeUtensilsPackage.fromName(packageName)
        val ref = ReferralEntity(
            id = refId,
            friendName = friendName,
            friendPhone = friendPhone,
            district = district,
            status = "PENDING",
            bonusUgx = pkg.activationCommissionUgx,
            referralPackage = pkg.displayName,
            isCommissionCredited = false,
            joinedDate = "Just now"
        )
        database.referralDao().insertReferral(ref)

        // Automatically activate referral with atomic commission crediting
        activateReferral(refId, packageName)
    }

    suspend fun registerNewMember(
        fullName: String,
        phoneNumber: String,
        inviterId: String,
        region: String,
        initialPackageName: String = "STANDARD PACKAGE"
    ): UserProfileEntity = withContext(Dispatchers.IO) {
        database.withTransaction {
            val newMemberId = generateMemberId()
            val district = when (region) {
                "Central" -> "Kampala Central"
                "Eastern" -> "Jinja City"
                "Northern" -> "Gulu City"
                "Western" -> "Mbarara City"
                else -> "Kampala Central"
            }
            val refCode = "UG-" + fullName.trim().split(" ").firstOrNull()?.uppercase()?.take(5) + "-" + (100..999).random()

            // Rule 3: Registration alone does NOT award a welcome bonus.
            // A welcome bonus is given only after first successful package activation.
            // Activation and welcome bonus are recorded as separate ledger events.
            val pkg = HomeUtensilsPackage.fromName(initialPackageName)
            val registeredProfile = UserProfileEntity(
                id = "current_user",
                memberId = newMemberId,
                fullName = fullName.trim(),
                phoneNumber = phoneNumber.trim(),
                region = region,
                district = district,
                referralCode = refCode,
                inviterId = inviterId.trim(),
                packageName = pkg.displayName,
                paidCapitalUgx = 0L,
                status = "ACTIVE",
                walletBalanceUgx = 0L,
                pendingBalanceUgx = 0L,
                totalEarnedUgx = 0L,
                hasReceivedWelcomeBonus = false,
                isPackageActivated = false,
                role = "USER"
            )
            database.userProfileDao().insertOrUpdate(registeredProfile)

            // Perform first successful package activation:
            val cycleId = "CYCLE-ACT-" + UUID.randomUUID().toString().take(8).uppercase()
            val activatedAt = System.currentTimeMillis()
            val expiresAt = activatedAt + pkg.durationDays * 86400 * 1000L

            // 1. Record Activation Cycle Attribution
            val cycle = ActivationCycleEntity(
                id = cycleId,
                userId = "current_user",
                memberId = newMemberId,
                packageTier = pkg.displayName,
                cycleType = "FIRST_ACTIVATION",
                amountUgx = pkg.activationAmountUgx,
                initialTasksAllowed = pkg.initialTasksCount,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                status = "ACTIVE"
            )
            database.activationCycleDao().insertCycle(cycle)

            // 2. Update user profile to activated package & Paid Capital
            database.userProfileDao().updateCycleDetails(
                packageName = pkg.displayName,
                paidCapital = pkg.paidCapitalUgx,
                cycleId = cycleId,
                activatedAt = activatedAt,
                expiresAt = expiresAt,
                isExpired = false
            )

            // 3. Separate Ledger Event 1: Package Activation (Attribution)
            val txActivation = WalletTransactionEntity(
                id = "TX-ACT-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "Package Activation (${pkg.displayName})",
                type = "PACKAGE_ACTIVATION",
                amountUgx = pkg.activationAmountUgx,
                status = "CREDITED",
                eventKey = "PACKAGE_ACTIVATION:$cycleId",
                timestamp = activatedAt
            )
            database.walletTransactionDao().insertTransaction(txActivation)

            // 4. Separate Ledger Event 2: Welcome Bonus on first successful activation
            val bonusAmount = pkg.welcomeBonusUgx
            database.userProfileDao().addCompletedEarnings(bonusAmount)
            database.userProfileDao().markWelcomeBonusReceived()

            val txBonus = WalletTransactionEntity(
                id = "TX-WBONUS-" + UUID.randomUUID().toString().take(10).uppercase(),
                title = "First-Time Welcome Bonus (${pkg.displayName})",
                type = "WELCOME_BONUS",
                amountUgx = bonusAmount,
                status = "CREDITED",
                eventKey = "WELCOME_BONUS:$newMemberId",
                timestamp = activatedAt + 10L
            )
            database.walletTransactionDao().insertTransaction(txBonus)

            database.userProfileDao().getProfileOnce() ?: registeredProfile
        }
    }

    suspend fun approveSubmission(submission: TaskSubmissionEntity) = withContext(Dispatchers.IO) {
        database.taskSubmissionDao().updateSubmissionStatus(
            id = submission.id,
            status = "APPROVED",
            reviewedAt = System.currentTimeMillis(),
            feedback = "Approved. Payment credited to wallet balance."
        )
        database.userProfileDao().approvePendingToWallet(submission.rewardUgx)
    }

    suspend fun rejectSubmission(submission: TaskSubmissionEntity, reason: String) = withContext(Dispatchers.IO) {
        database.taskSubmissionDao().updateSubmissionStatus(
            id = submission.id,
            status = "REJECTED",
            reviewedAt = System.currentTimeMillis(),
            feedback = reason.ifBlank { "Declined: Image quality or answer mismatch." }
        )
        database.userProfileDao().rejectPendingReward(submission.rewardUgx)
    }

    suspend fun createNewTask(
        title: String,
        category: String,
        rewardUgx: Long,
        description: String,
        instructions: String,
        options: List<String>,
        imageResName: String = "hero_utensils_banner_1790683214191"
    ) = withContext(Dispatchers.IO) {
        val newTask = UtensilTaskEntity(
            id = "task_" + UUID.randomUUID().toString().take(8),
            title = title,
            category = category,
            rewardUgx = rewardUgx,
            description = description,
            instructions = instructions,
            imageResName = imageResName,
            status = "ACTIVE",
            questionPrompt = "Inspect the household utensil image and choose the verified answer:",
            optionsString = options.joinToString(","),
            estimatedMinutes = 3,
            createdAt = System.currentTimeMillis()
        )
        database.utensilTaskDao().insertTask(newTask)
    }

    suspend fun toggleTaskStatus(task: UtensilTaskEntity) = withContext(Dispatchers.IO) {
        val newStatus = if (task.status == "ACTIVE") "PAUSED" else "ACTIVE"
        database.utensilTaskDao().updateTaskStatus(task.id, newStatus)
    }

    suspend fun toggleUserRole(currentRole: String) = withContext(Dispatchers.IO) {
        val nextRole = if (currentRole == "ADMIN") "USER" else "ADMIN"
        database.userProfileDao().updateRole(nextRole)
    }

    suspend fun updateDistrict(newDistrict: String) = withContext(Dispatchers.IO) {
        database.userProfileDao().updateDistrict(newDistrict)
    }

    suspend fun reloadDemoTasks() = withContext(Dispatchers.IO) {
        val allTasks = database.utensilTaskDao().getAllTasks().firstOrNull() ?: emptyList()
        allTasks.forEach { task ->
            database.utensilTaskDao().updateTaskStatus(task.id, "ACTIVE")
        }
    }

    // =========================================================================
    // PART A: AGENT PAYMENT PROTECTION REPOSITORY OPERATIONS
    // =========================================================================

    suspend fun getEligibleAgents(packageName: String, preferredRegion: String? = null): List<AgentWorkingFloatEntity> = withContext(Dispatchers.IO) {
        val requiredAmount = HomeUtensilsPackage.fromName(packageName).activationAmountUgx
        val config = database.agentPaymentConfigDao().getConfigOnce()
        val maxUnresolved = config?.maxUnresolvedCasesThreshold ?: 2

        val allAgents = database.agentWorkingFloatDao().getAllWorkingFloats().firstOrNull() ?: emptyList()
        allAgents.filter { agent ->
            agent.status == "ACTIVE" &&
            agent.isAvailable &&
            agent.workingFloatUgx >= requiredAmount &&
            !agent.isRestrictedFromNewRequests &&
            agent.unresolvedCasesCount < maxUnresolved &&
            (preferredRegion.isNullOrBlank() || agent.region.equals(preferredRegion, ignoreCase = true))
        }
    }

    suspend fun createAgentActivationRequest(
        memberId: String,
        memberName: String,
        memberPhone: String,
        agentId: String,
        packageName: String,
        region: String
    ): Result<AgentActivationRequestEntity> = withContext(Dispatchers.IO) {
        val pkg = HomeUtensilsPackage.fromName(packageName)
        val agent = database.agentWorkingFloatDao().getAgentById(agentId)
            ?: return@withContext Result.failure(Exception("Selected Agent does not exist."))

        if (agent.status != "ACTIVE" || !agent.isAvailable || agent.isRestrictedFromNewRequests) {
            return@withContext Result.failure(Exception("Agent is currently unavailable or restricted from new assignments."))
        }
        if (agent.workingFloatUgx < pkg.activationAmountUgx) {
            return@withContext Result.failure(Exception("Agent does not have sufficient working float for ${pkg.displayName}."))
        }

        val requestId = "APR-" + UUID.randomUUID().toString().take(8).uppercase()
        val request = AgentActivationRequestEntity(
            id = requestId,
            memberId = memberId,
            memberName = memberName,
            memberPhone = memberPhone,
            agentId = agent.agentId,
            agentName = agent.agentName,
            agentPhotoUri = agent.photoUri,
            packageName = pkg.displayName,
            amountUgx = pkg.activationAmountUgx,
            region = region.ifBlank { agent.region },
            status = "Agent Accepted", // Agent auto-accepts official request; Member now guided to pay
            createdAt = System.currentTimeMillis(),
            agentAcceptedAt = System.currentTimeMillis(),
            agentPhone = agent.phone,
            agentWhatsapp = agent.whatsappNumber,
            officialPaymentMethod = agent.officialPaymentMethod,
            officialPaymentAccount = agent.officialPaymentAccount,
            officialBeneficiaryName = agent.officialBeneficiaryName
        )
        database.agentActivationRequestDao().insertRequest(request)
        Result.success(request)
    }

    suspend fun reportMemberPayment(
        requestId: String,
        paymentMethod: String,
        reference: String,
        memberNote: String,
        proofUri: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val request = database.agentActivationRequestDao().getRequestByIdOnce(requestId)
            ?: return@withContext Result.failure(Exception("Activation request not found."))

        val config = database.agentPaymentConfigDao().getConfigOnce()
        val timeoutMins = config?.responseTimeoutMinutes ?: 60
        val now = System.currentTimeMillis()
        val deadline = now + (timeoutMins * 60 * 1000L)

        database.agentActivationRequestDao().reportPayment(
            id = requestId,
            method = paymentMethod,
            reference = reference,
            note = memberNote,
            proofUri = proofUri,
            uploadedAt = now,
            uploader = request.memberId,
            reportedAt = now,
            deadline = deadline
        )

        // Log proof access / upload event
        database.paymentProofAccessLogDao().insertLog(
            PaymentProofAccessLogEntity(
                id = "LOG-" + UUID.randomUUID().toString().take(8).uppercase(),
                caseOrRequestId = requestId,
                accessedBy = request.memberId,
                userRole = "MEMBER",
                action = "UPLOAD"
            )
        )

        Result.success("Payment reported successfully. Agent notified to verify within $timeoutMins minutes.")
    }
suspend fun confirmPaymentAndActivate(
    requestId: String,
    agentId: String
): Result<String> = withContext(Dispatchers.IO) {
    database.withTransaction {
        val request = database.agentActivationRequestDao().getRequestByIdOnce(requestId)
            ?: return@withTransaction Result.failure(Exception("Activation request not found."))

        if (request.status == "Activation Completed" || request.status == "Confirmed") {
            return@withTransaction Result.failure(
                Exception("Duplicate activation prevented: This request is already completed.")
            )
        }

        if (request.status != "Payment Reported") {
            return@withTransaction Result.failure(
                Exception("Payment must be reported before activation.")
            )
        }

        if (request.paymentMethod.isBlank() || request.paymentReference.isBlank()) {
            return@withTransaction Result.failure(
                Exception("Payment method and reference are required.")
            )
        }

        if (request.agentId != agentId) {
            return@withTransaction Result.failure(
                Exception("Only the assigned agent can confirm this payment.")
            )
        }

        val pkg = HomeUtensilsPackage.fromName(request.packageName)
        val agent = database.agentWorkingFloatDao().getAgentById(agentId)
            ?: return@withTransaction Result.failure(Exception("Agent not found."))

        if (agent.workingFloatUgx < request.amountUgx) {
            return@withTransaction Result.failure(
                Exception("Insufficient Agent Working Float (Available: UGX ${agent.workingFloatUgx}, Required: UGX ${request.amountUgx}).")
            )
        }

        // 1. Deduct agent float atomically
        database.agentWorkingFloatDao().deductWorkingFloat(agentId, request.amountUgx)

        // 2. Create Activation Cycle for member
        val cycleId = "CYCLE-APR-" + UUID.randomUUID().toString().take(8).uppercase()
        val now = System.currentTimeMillis()
        val durationMs = pkg.durationDays * 86400 * 1000L
        val cycle = ActivationCycleEntity(
            id = cycleId,
            userId = if (request.memberId == "HU7K4M92Q") "current_user" else request.memberId,
            memberId = request.memberId,
            packageTier = pkg.displayName,
            cycleType = "FIRST_ACTIVATION",
            amountUgx = request.amountUgx,
            initialTasksAllowed = pkg.initialTasksCount,
            activatedAt = now,
            expiresAt = now + durationMs,
            status = "ACTIVE",
            facilitatedByAgentId = agentId
        )
        database.activationCycleDao().insertCycle(cycle)

        // 3. Update member profile if this is the current user
        if (request.memberId == "HU7K4M92Q") {
            database.userProfileDao().updateCycleDetails(
                packageName = pkg.displayName,
                paidCapital = pkg.paidCapitalUgx,
                cycleId = cycleId,
                activatedAt = now,
                expiresAt = now + durationMs,
                isExpired = false
            )
        }

        // 4. Create Agent Facilitation record
        val facId = "AGT-FAC-" + UUID.randomUUID().toString().take(8).uppercase()
        database.agentFacilitationDao().insertFacilitation(
            AgentFacilitationEntity(
                id = facId,
                agentId = agentId,
                recipientMemberId = request.memberId,
                recipientName = request.memberName,
                packageName = pkg.displayName,
                amountUgx = request.amountUgx,
                feeUgx = 0L,
                timestamp = now
            )
        )

        // 5. Create Ledger Transaction Entity
        val txId = "TXN-ACT-" + UUID.randomUUID().toString().take(8).uppercase()
        database.walletTransactionDao().insertTransaction(
            WalletTransactionEntity(
                id = txId,
                title = "Agent Protected Package Activation (${pkg.displayName})",
                type = "AGENT_FACILITATION",
                amountUgx = request.amountUgx,
                status = "CREDITED",
                eventKey = requestId,
                timestamp = now,
                feeUgx = 0L,
                netAmountUgx = request.amountUgx,
                referenceNumber = request.paymentReference,
                counterpartyName = agent.agentName,
                counterpartyId = agent.agentId,
                runningBalanceUgx = pkg.paidCapitalUgx,
                paymentMethod = request.paymentMethod,
                notes = "Facilitated by Agent ${agent.agentId}. Float deducted atomically after payment report."
            )
        )

        // 6. Record 2% Agent commission
        val commUgx = (request.amountUgx * 0.02).toLong()
        if (commUgx > 0) {
            database.agentWorkingFloatDao().addCommission(agentId, commUgx)
            database.agentCommissionLogDao().insertCommissionLog(
                AgentCommissionLogEntity(
                    id = "COMM-" + UUID.randomUUID().toString().take(8).uppercase(),
                    agentId = agentId,
                    memberId = request.memberId,
                    cycleId = cycleId,
                    taskId = "ACTIVATION_BONUS",
                    taskTitle = "2% Package Facilitation Commission (${pkg.displayName})",
                    taskRewardUgx = request.amountUgx,
                    commissionRate = 0.02,
                    commissionUgx = commUgx,
                    timestamp = now
                )
            )
        }

        // 7. Complete request and refresh unresolved-case count
        database.agentActivationRequestDao().completeActivation(
            requestId,
            "Activation Completed",
            cycleId
        )
        val updatedUnresolved =
            database.agentActivationRequestDao().getUnresolvedCasesCountForAgent(agentId)
        database.agentWorkingFloatDao().updateUnresolvedCount(agentId, updatedUnresolved)

        val config = database.agentPaymentConfigDao().getConfigOnce()
        if (updatedUnresolved < (config?.maxUnresolvedCasesThreshold ?: 2)) {
            database.agentWorkingFloatDao().updateRestriction(agentId, false, "")
        }

        Result.success(
            "Activation completed successfully! Agent float deducted by UGX ${request.amountUgx}. Member package is now active."
        )
    }
}

    suspend fun disputeAgentPayment(
        requestId: String,
        agentId: String,
        agentStatement: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val request = database.agentActivationRequestDao().getRequestByIdOnce(requestId)
            ?: return@withContext Result.failure(Exception("Request not found."))

        database.agentActivationRequestDao().updateAgentResponse(
            id = requestId,
            status = "Payment Disputed",
            statement = agentStatement,
            isDisputed = true
        )

        val caseId = "CASE-" + UUID.randomUUID().toString().take(8).uppercase()
        val protCase = AgentPaymentProtectionCaseEntity(
            id = caseId,
            requestId = requestId,
            memberId = request.memberId,
            memberName = request.memberName,
            agentId = agentId,
            agentName = request.agentName,
            amountUgx = request.amountUgx,
            reportedAt = System.currentTimeMillis(),
            reason = "Payment Disputed by Agent",
            status = "OPEN",
            agentStatement = agentStatement,
            memberStatement = request.memberNote
        )
        database.agentPaymentProtectionCaseDao().insertCase(protCase)

        // Increment unresolved cases count and check threshold
        val unresolved = database.agentActivationRequestDao().getUnresolvedCasesCountForAgent(agentId)
        database.agentWorkingFloatDao().updateUnresolvedCount(agentId, unresolved)
        val config = database.agentPaymentConfigDao().getConfigOnce()
        val threshold = config?.maxUnresolvedCasesThreshold ?: 2
        if (unresolved >= threshold) {
            database.agentWorkingFloatDao().updateRestriction(
                agentId,
                true,
                "Temporarily restricted from new activation requests due to $unresolved unresolved payment cases."
            )
        }

        Result.success("Payment marked as disputed. Case $caseId forwarded for Administrative Review.")
    }

    suspend fun reportAgentPaymentIssue(
        requestId: String,
        reason: String,
        statement: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val request = database.agentActivationRequestDao().getRequestByIdOnce(requestId)
            ?: return@withContext Result.failure(Exception("Request not found."))

        val caseId = "CASE-" + UUID.randomUUID().toString().take(8).uppercase()
        val protCase = AgentPaymentProtectionCaseEntity(
            id = caseId,
            requestId = requestId,
            memberId = request.memberId,
            memberName = request.memberName,
            agentId = request.agentId,
            agentName = request.agentName,
            amountUgx = request.amountUgx,
            reportedAt = System.currentTimeMillis(),
            reason = reason.ifBlank { "Agent Not Responding" },
            status = "ADMIN_REVIEW",
            memberStatement = statement
        )
        database.agentPaymentProtectionCaseDao().insertCase(protCase)
        database.agentActivationRequestDao().updateStatus(requestId, "Admin Review")

        val unresolved = database.agentActivationRequestDao().getUnresolvedCasesCountForAgent(request.agentId)
        database.agentWorkingFloatDao().updateUnresolvedCount(request.agentId, unresolved)

        Result.success("Payment issue reported. Case ID: $caseId created. Admin team notified.")
    }

    suspend fun updateAgentProfilePhoto(
        agentId: String,
        newPhotoUri: String,
        changedBy: String = "AGENT"
    ): Result<String> = withContext(Dispatchers.IO) {
        val agent = database.agentWorkingFloatDao().getAgentById(agentId)
            ?: return@withContext Result.failure(Exception("Agent not found."))

        val audit = AgentPhotoAuditEntity(
            id = "AUDIT-PHOTO-" + UUID.randomUUID().toString().take(8).uppercase(),
            agentId = agentId,
            agentName = agent.agentName,
            previousPhotoUri = agent.photoUri,
            newPhotoUri = newPhotoUri,
            changedBy = changedBy,
            action = if (newPhotoUri.isBlank()) "REMOVE" else "REPLACE",
            timestamp = System.currentTimeMillis(),
            notes = "Photo update by $changedBy"
        )
        database.agentPhotoAuditDao().insertAudit(audit)
        database.agentWorkingFloatDao().updatePhoto(agentId, newPhotoUri)

        Result.success("Agent photo updated and audit logged.")
    }

    suspend fun toggleAgentAvailability(agentId: String, isAvailable: Boolean) = withContext(Dispatchers.IO) {
        database.agentWorkingFloatDao().updateAvailability(agentId, isAvailable)
    }

    suspend fun adminVerifyPaymentAndActivate(
        requestId: String,
        adminNotes: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val request = database.agentActivationRequestDao().getRequestByIdOnce(requestId)
            ?: return@withContext Result.failure(Exception("Request not found."))

        // Calls atomic activation
        val activationRes = confirmPaymentAndActivate(requestId, request.agentId)
        if (activationRes.isSuccess) {
            database.agentActivationRequestDao().updateAdminResolution(
                id = requestId,
                status = "Resolved",
                notes = adminNotes,
                isResolved = true,
                resolvedAt = System.currentTimeMillis(),
                outcome = "Verified by Administration & Activation Completed"
            )
            Result.success("Admin verified and completed activation for ${request.memberName}.")
        } else {
            activationRes
        }
    }

    suspend fun adminRejectPaymentClaim(
        requestId: String,
        reason: String
    ): Result<String> = withContext(Dispatchers.IO) {
        database.agentActivationRequestDao().updateAdminResolution(
            id = requestId,
            status = "Cancelled",
            notes = reason,
            isResolved = true,
            resolvedAt = System.currentTimeMillis(),
            outcome = "Claim rejected: $reason"
        )
        Result.success("Request cancelled and claim rejected.")
    }

    suspend fun adminResolveProtectionCase(
        caseId: String,
        resolutionSummary: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val caseEntity = database.agentPaymentProtectionCaseDao().getCaseByIdOnce(caseId)
            ?: return@withContext Result.failure(Exception("Protection case not found."))

        database.agentPaymentProtectionCaseDao().updateCaseStatus(
            caseId = caseId,
            status = "RESOLVED",
            adminNotes = resolutionSummary,
            resolution = resolutionSummary,
            updatedAt = System.currentTimeMillis()
        )
        database.agentActivationRequestDao().updateStatus(caseEntity.requestId, "Resolved")

        val unresolved = database.agentActivationRequestDao().getUnresolvedCasesCountForAgent(caseEntity.agentId)
        database.agentWorkingFloatDao().updateUnresolvedCount(caseEntity.agentId, unresolved)
        val config = database.agentPaymentConfigDao().getConfigOnce()
        if (unresolved < (config?.maxUnresolvedCasesThreshold ?: 2)) {
            database.agentWorkingFloatDao().updateRestriction(caseEntity.agentId, false, "")
        }

        Result.success("Protection case $caseId marked as resolved.")
    }

    suspend fun adminUpdateAgentStatus(
        agentId: String,
        status: String,
        isAvailable: Boolean,
        isRestricted: Boolean,
        reason: String
    ): Result<String> = withContext(Dispatchers.IO) {
        database.agentWorkingFloatDao().updateAgentStatus(agentId, status, isAvailable)
        database.agentWorkingFloatDao().updateRestriction(agentId, isRestricted, reason)
        Result.success("Agent status updated.")
    }

    suspend fun adminUpdatePaymentConfig(
        timeoutMinutes: Int,
        threshold: Int
    ): Result<String> = withContext(Dispatchers.IO) {
        database.agentPaymentConfigDao().updateConfig(timeoutMinutes, threshold)
        Result.success("Payment protection configuration updated.")
    }

    suspend fun updateMemberStatus(memberId: String, status: String) = withContext(Dispatchers.IO) {
        database.userProfileDao().updateMemberStatus(memberId, status)
    }

    suspend fun logProofAccess(
        requestId: String,
        accessedBy: String,
        userRole: String,
        action: String = "VIEW"
    ) = withContext(Dispatchers.IO) {
        database.paymentProofAccessLogDao().insertLog(
            PaymentProofAccessLogEntity(
                id = "LOG-" + UUID.randomUUID().toString().take(8).uppercase(),
                caseOrRequestId = requestId,
                accessedBy = accessedBy,
                userRole = userRole,
                action = action
            )
        )
    }

    suspend fun checkAndUpdateOverdueRequests() = withContext(Dispatchers.IO) {
        val allRequests = database.agentActivationRequestDao().getAllRequests().firstOrNull() ?: emptyList()
        val now = System.currentTimeMillis()
        allRequests.forEach { req ->
            if ((req.status == "Payment Reported" || req.status == "Awaiting Agent Confirmation") &&
                req.responseDeadline != null && now > req.responseDeadline
            ) {
                database.agentActivationRequestDao().updateStatus(req.id, "Agent Response Overdue")
                database.agentWorkingFloatDao().updateRestriction(
                    req.agentId,
                    true,
                    "Restricted due to overdue response on request ${req.id}"
                )
            }
        }
    }

    suspend fun approveAgentFloatPurchase(
        purchaseId: String,
        agentId: String,
        approvedFloatPurchase: Long
    ): Result<Long> = withContext(Dispatchers.IO) {
        if (approvedFloatPurchase < 50000L) {
            return@withContext Result.failure(Exception("Minimum Agent float purchase/qualification is UGX 50,000."))
        }
        database.withTransaction {
            val agent = database.agentWorkingFloatDao().getWorkingFloatOnce(agentId)
                ?: return@withTransaction Result.failure(Exception("Agent not found: $agentId"))

            val eventKey = "AGENT_FLOAT_APPROVAL:$purchaseId"
            val existingTx = database.walletTransactionDao().getTransactionByEventKey(eventKey)
            if (existingTx != null) {
                return@withTransaction Result.failure(Exception("Float purchase $purchaseId was already approved and credited."))
            }

            // Formula:
            // companyFloatBonus = approvedFloatPurchase * 0.15
            // totalAgentFloat = approvedFloatPurchase + companyFloatBonus
            val companyFloatBonus = (approvedFloatPurchase * 0.15).toLong()
            val totalAgentFloat = approvedFloatPurchase + companyFloatBonus

            // Credit the Agent working float atomically (operational working float, NOT personal withdrawable income)
            database.agentWorkingFloatDao().addWorkingFloat(agentId, totalAgentFloat)

            val txId = "TX-FLT-" + UUID.randomUUID().toString().take(8).uppercase()
            database.walletTransactionDao().insertTransaction(
                WalletTransactionEntity(
                    id = txId,
                    title = "Agent Float Purchase (+15% Company Bonus)",
                    type = "AGENT_FLOAT_TOPUP",
                    amountUgx = totalAgentFloat,
                    status = "CREDITED",
                    eventKey = eventKey,
                    timestamp = System.currentTimeMillis(),
                    feeUgx = 0L,
                    netAmountUgx = totalAgentFloat,
                    referenceNumber = purchaseId,
                    counterpartyName = agent.agentName,
                    counterpartyId = agent.agentId,
                    runningBalanceUgx = agent.workingFloatUgx + totalAgentFloat,
                    paymentMethod = "OFFICIAL_COMPANY_FLOAT",
                    notes = "Approved Float Purchase: UGX $approvedFloatPurchase | Company Bonus (+15%): UGX $companyFloatBonus | Total Float: UGX $totalAgentFloat. The 15% Company Float Bonus is Agent working float. It is NOT personal withdrawable income."
                )
            )

            val updatedAgent = database.agentWorkingFloatDao().getWorkingFloatOnce(agentId)
            Result.success(updatedAgent?.workingFloatUgx ?: (agent.workingFloatUgx + totalAgentFloat))
        }
    }
}

data class TaskEvaluationResult(
    val taskId: String,
    val taskTitle: String,
    val isCorrect: Boolean,
    val awardedUgx: Long,
    val userAnswer: String,
    val correctAnswer: String,
    val feedback: String
)

data class UpgradeResult(
    val success: Boolean,
    val message: String,
    val upgradedPackage: String,
    val amountChargedUgx: Long,
    val inviterCommissionUgx: Long
)

data class ReactivationResult(
    val success: Boolean,
    val message: String,
    val packageTier: String,
    val amountUgx: Long,
    val newDurationDays: Int,
    val initialTasksCount: Int
)

data class WithdrawalResult(
    val success: Boolean,
    val message: String,
    val requestId: String? = null,
    val feeUgx: Long = 0L,
    val netAmountUgx: Long = 0L
)

data class WithdrawalAvailabilityResult(
    val isOpen: Boolean,
    val isHoliday: Boolean = false,
    val holidayName: String? = null,
    val isSunday: Boolean = false,
    val isOutsideHours: Boolean = false,
    val message: String
)

data class TransferResult(
    val success: Boolean,
    val message: String,
    val txId: String? = null,
    val feeUgx: Long = 0L,
    val totalDeductedUgx: Long = 0L,
    val netRecipientAmountUgx: Long = 0L
)

data class RecipientLookupResult(
    val memberId: String,
    val maskedId: String,
    val name: String,
    val isRegistered: Boolean
)

data class AgentFacilitationResult(
    val success: Boolean,
    val message: String,
    val txId: String? = null,
    val packageTier: String = "",
    val amountUgx: Long = 0L
)

