package org.consumersunion.stories.common.server.service.datatransferobject;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ConvioConstituent implements IsSerializable {
    @JsonIgnore
    private Integer sid;
    private String valid;
    private String lifetime_alert_response_count;
    private String user_name;
    private String trans_mon_val_percentile;
    private String work_phone;
    private String accepts_postal_mail;
    private String home_fax;
    private String origin;
    private String trans_frequency_percentile;
    private String home_phone;
    private String prev_year_alert_response_count;
    private String marital_status;
    private String nickname;
    private String gender;
    private String curr_year_alert_response_count;
    private String salutation_casual;
    private String member_id;
    private String employment;
    private String origin_source;
    private String matched_donor;
    private String trans_recency_percentile;
    private String origin_subsource;
    private String cons_id;
    private String salutation_formal;
    private String donor_status;
    private String mobile_phone;
    private String exchange_id;
    private String dir_member_opt_in;
    private ConvioNameObject name;
    private ConvioAddressObject primary_address;
    private ConvioAddressObject alternate_address1;
    private ConvioAddressObject alternate_address2;
    private ConvioEmailObject email;

    // TODO: Create those classes then remove annotations and change attributes type.
    @JsonIgnore
    private String districts;/*: {
            home_county;
			state_house_dist_id_override;
			state_house_dist_id;
			state_senate_dist_id_override;
			cong_dist_id;
			home_county_override;
			state_senate_dist_id;
			cong_dist_id_override;
		}-*/
    @JsonIgnore
    private String active;/*: {
            detail;
			status;
		}-*/
    @JsonIgnore
    private String engagement_factors;/*: {
			factor: [10]					-
		}-*/
    @JsonIgnore
    private String sustained_gift;/*: {
			status;
			monthly_amount;
		}-*/

    @JsonIgnore
    private String membership; /*: {
			status;
			id;
		}-*/

    public String getLifetime_alert_response_count() {
        return lifetime_alert_response_count;
    }

    public void setLifetime_alert_response_count(String lifetime_alert_response_count) {
        this.lifetime_alert_response_count = lifetime_alert_response_count;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTrans_mon_val_percentile() {
        return trans_mon_val_percentile;
    }

    public void setTrans_mon_val_percentile(String trans_mon_val_percentile) {
        this.trans_mon_val_percentile = trans_mon_val_percentile;
    }

    public String getWork_phone() {
        return work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }

    public String getAccepts_postal_mail() {
        return accepts_postal_mail;
    }

    public void setAccepts_postal_mail(String accepts_postal_mail) {
        this.accepts_postal_mail = accepts_postal_mail;
    }

    public String getHome_fax() {
        return home_fax;
    }

    public void setHome_fax(String home_fax) {
        this.home_fax = home_fax;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public ConvioAddressObject getAlternate_address2() {
        return alternate_address2;
    }

    public void setAlternate_address2(ConvioAddressObject alternate_address2) {
        this.alternate_address2 = alternate_address2;
    }

    public String getTrans_frequency_percentile() {
        return trans_frequency_percentile;
    }

    public void setTrans_frequency_percentile(String trans_frequency_percentile) {
        this.trans_frequency_percentile = trans_frequency_percentile;
    }

    public String getHome_phone() {
        return home_phone;
    }

    public void setHome_phone(String home_phone) {
        this.home_phone = home_phone;
    }

    public String getPrev_year_alert_response_count() {
        return prev_year_alert_response_count;
    }

    public void setPrev_year_alert_response_count(String prev_year_alert_response_count) {
        this.prev_year_alert_response_count = prev_year_alert_response_count;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCurr_year_alert_response_count() {
        return curr_year_alert_response_count;
    }

    public void setCurr_year_alert_response_count(String curr_year_alert_response_count) {
        this.curr_year_alert_response_count = curr_year_alert_response_count;
    }

    public String getSalutation_casual() {
        return salutation_casual;
    }

    public void setSalutation_casual(String salutation_casual) {
        this.salutation_casual = salutation_casual;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getOrigin_source() {
        return origin_source;
    }

    public void setOrigin_source(String origin_source) {
        this.origin_source = origin_source;
    }

    public String getMatched_donor() {
        return matched_donor;
    }

    public void setMatched_donor(String matched_donor) {
        this.matched_donor = matched_donor;
    }

    public String getTrans_recency_percentile() {
        return trans_recency_percentile;
    }

    public void setTrans_recency_percentile(String trans_recency_percentile) {
        this.trans_recency_percentile = trans_recency_percentile;
    }

    public String getOrigin_subsource() {
        return origin_subsource;
    }

    public void setOrigin_subsource(String origin_subsource) {
        this.origin_subsource = origin_subsource;
    }

    public String getCons_id() {
        return cons_id;
    }

    public void setCons_id(String cons_id) {
        this.cons_id = cons_id;
    }

    public String getSalutation_formal() {
        return salutation_formal;
    }

    public void setSalutation_formal(String salutation_formal) {
        this.salutation_formal = salutation_formal;
    }

    public String getDonor_status() {
        return donor_status;
    }

    public void setDonor_status(String donor_status) {
        this.donor_status = donor_status;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getExchange_id() {
        return exchange_id;
    }

    public void setExchange_id(String exchange_id) {
        this.exchange_id = exchange_id;
    }

    public ConvioAddressObject getAlternate_address1() {
        return alternate_address1;
    }

    public void setAlternate_address1(ConvioAddressObject alternate_address1) {
        this.alternate_address1 = alternate_address1;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEngagement_factors() {
        return engagement_factors;
    }

    public void setEngagement_factors(String engagement_factors) {
        this.engagement_factors = engagement_factors;
    }

    public String getSustained_gift() {
        return sustained_gift;
    }

    public void setSustained_gift(String sustained_gift) {
        this.sustained_gift = sustained_gift;
    }

    public ConvioEmailObject getEmail() {
        return email;
    }

    public void setEmail(ConvioEmailObject email) {
        this.email = email;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public ConvioNameObject getName() {
        return name;
    }

    public void setName(ConvioNameObject name) {
        this.name = name;
    }

    public ConvioAddressObject getPrimary_address() {
        return primary_address;
    }

    public void setPrimary_address(ConvioAddressObject primary_address) {
        this.primary_address = primary_address;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getDir_member_opt_in() {
        return dir_member_opt_in;
    }

    public void setDir_member_opt_in(String dir_member_opt_in) {
        this.dir_member_opt_in = dir_member_opt_in;
    }
}
