package org.consumersunion.stories.common.server.service.datatransferobject;

import com.google.common.base.Strings;

public class ConvioRequestUrlBuilder {

    public static String buildRequestUrl(String urlBase, ConvioConstituent convioMainObject) {
        if (!Strings.isNullOrEmpty(convioMainObject.getUser_name())) {
            urlBase += "&primary_email=" + convioMainObject.getUser_name();
        }

        if (!Strings.isNullOrEmpty(convioMainObject.getHome_phone())) {
            urlBase += "&home_phone=" + convioMainObject.getHome_phone();
        } else {
            urlBase += "&home_phone= ";
        }
        if (!Strings.isNullOrEmpty(convioMainObject.getWork_phone())) {
            urlBase += "&work_phone=" + convioMainObject.getWork_phone();
        } else {
            urlBase += "&work_phone= ";
        }
        if (!Strings.isNullOrEmpty(convioMainObject.getMobile_phone())) {
            urlBase += "&mobile_phone=" + convioMainObject.getMobile_phone();
        } else {
            urlBase += "&mobile_phone= ";
        }
        if (!Strings.isNullOrEmpty(convioMainObject.getHome_fax())) {
            urlBase += "&home_fax=" + convioMainObject.getHome_fax();
        } else {
            urlBase += "&home_fax= ";
        }

        if (convioMainObject.getName() != null) {
            if (!Strings.isNullOrEmpty(convioMainObject.getName().getFirst())) {
                urlBase += "&name.first=" + convioMainObject.getName().getFirst();
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getName().getLast())) {
                urlBase += "&name.last=" + convioMainObject.getName().getLast();
            }
        }

        if (convioMainObject.getEmail() != null) {
            if (!Strings.isNullOrEmpty(convioMainObject.getEmail().getPrimary_address())) {
                urlBase += "&email.primary_address=" + convioMainObject.getEmail().getPrimary_address();
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getEmail().getSecondary_address())) {
                urlBase += "&email.secondary_address=" + convioMainObject.getEmail().getSecondary_address();
            } else {
                urlBase += "&email.secondary_address= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getEmail().getPreferred_format())) {
                urlBase += "&email.preferred_format=" + convioMainObject.getEmail().getPreferred_format();
            }
        }

        if (!Strings.isNullOrEmpty(convioMainObject.getDir_member_opt_in())) {
            urlBase += "&dir_member_opt_in=" + convioMainObject.getDir_member_opt_in();
        }

        if (convioMainObject.getPrimary_address() != null) {
            if (!Strings.isNullOrEmpty(convioMainObject.getPrimary_address().getCity())) {
                urlBase += "&primary_address.city=" + convioMainObject.getPrimary_address().getCity();
            } else {
                urlBase += "&primary_address.city= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getPrimary_address().getCountry())) {
                urlBase += "&primary_address.country=" + convioMainObject.getPrimary_address().getCountry();
            } else {
                urlBase += "&primary_address.country= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getPrimary_address().getState())) {
                urlBase += "&primary_address.state=" + convioMainObject.getPrimary_address().getState();
            } else {
                urlBase += "&primary_address.state= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getPrimary_address().getZip())) {
                urlBase += "&primary_address.zip=" + convioMainObject.getPrimary_address().getZip();
            } else {
                urlBase += "&primary_address.zip= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getPrimary_address().getStreet1())) {
                urlBase += "&primary_address.street1=" + convioMainObject.getPrimary_address().getStreet1();
            } else {
                urlBase += "&primary_address.street1= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getPrimary_address().getStreet2())) {
                urlBase += "&primary_address.street2=" + convioMainObject.getPrimary_address().getStreet2();
            } else {
                urlBase += "&primary_address.street2= ";
            }
        }

        if (convioMainObject.getAlternate_address1() != null) {
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address1().getCity())) {
                urlBase += "&alternate_address1.city=" + convioMainObject.getAlternate_address1().getCity();
            } else {
                urlBase += "&alternate_address1.city= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address1().getCountry())) {
                urlBase += "&alternate_address1.country=" + convioMainObject.getAlternate_address1().getCountry();
            } else {
                urlBase += "&alternate_address1.country= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address1().getState())) {
                urlBase += "&alternate_address1.state=" + convioMainObject.getAlternate_address1().getState();
            } else {
                urlBase += "&alternate_address1.state= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address1().getZip())) {
                urlBase += "&alternate_address1.zip=" + convioMainObject.getAlternate_address1().getZip();
            } else {
                urlBase += "&alternate_address1.zip= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address1().getStreet1())) {
                urlBase += "&alternate_address1.street1=" + convioMainObject.getAlternate_address1().getStreet1();
            } else {
                urlBase += "&alternate_address1.street1= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address1().getStreet2())) {
                urlBase += "&alternate_address1.street2=" + convioMainObject.getAlternate_address1().getStreet2();
            } else {
                urlBase += "&alternate_address1.street2= ";
            }
        }

        if (convioMainObject.getAlternate_address2() != null) {
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address2().getCity())) {
                urlBase += "&alternate_address2.city=" + convioMainObject.getAlternate_address2().getCity();
            } else {
                urlBase += "&alternate_address2.city= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address2().getCountry())) {
                urlBase += "&alternate_address2.country=" + convioMainObject.getAlternate_address2().getCountry();
            } else {
                urlBase += "&alternate_address2.country= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address2().getState())) {
                urlBase += "&alternate_address2.state=" + convioMainObject.getAlternate_address2().getState();
            } else {
                urlBase += "&alternate_address2.state= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address2().getZip())) {
                urlBase += "&alternate_address2.zip=" + convioMainObject.getAlternate_address2().getZip();
            } else {
                urlBase += "&alternate_address2.zip= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address2().getStreet1())) {
                urlBase += "&alternate_address2.street1=" + convioMainObject.getAlternate_address2().getStreet1();
            } else {
                urlBase += "&alternate_address2.street1= ";
            }
            if (!Strings.isNullOrEmpty(convioMainObject.getAlternate_address2().getStreet2())) {
                urlBase += "&alternate_address2.street2=" + convioMainObject.getAlternate_address2().getStreet2();
            } else {
                urlBase += "&alternate_address2.street2= ";
            }
        }

        return urlBase;
    }
}
