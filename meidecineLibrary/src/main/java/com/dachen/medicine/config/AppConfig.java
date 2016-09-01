package com.dachen.medicine.config;

import com.dachen.medicine.common.utils.MedicineApplication;
import com.dachen.medicine.net.NetConfig;

import java.io.File;
import java.util.Map;

public class AppConfig {
	public static final String PACKAGENAME_PACIENT_XUANGUAN = "com.dachen.dgrouppatient";
	public static final String PACKAGENAME_PACIENT_BOGE = "com.bestunimed.dgrouppatient";
	public static final String PACKAGENAME_DOCTOR_XUANGUAN = "com.dachen.dgroupdoctor";
	public static final String PACKAGENAME_DOCTOR_BOGE = "com.bestunimed.dgroupdoctor";
	public static final String PACKAGENAME_DOCTOR_LIBRARY = "com.dachen.mediecinelibraryrealizedoctor";
	public static final String PACKAGENAME_PACIENT_LIBRAY = "com.dachen.mediecinelibraryrealize";
	public static String getUrl(String interfaceName, int net) {
		String url = "";
		 if(net==NetConfig.HIPPOSERVER){
			 url = MedicineApplication.getUrl() + File.separator
					 + interfaceName;
		 }else if(net == NetConfig.MEDIESERVER){

			 if (MedicineApplication.getUserType().equals("1")||MedicineApplication.getUserType().equals("3")
					 ||MedicineApplication.getUserType().equals("10")){
				 url = MedicineApplication.getIP()+":"+ NetConfig.port + File.separator
						 + interfaceName;
			 }else {
				 url = MedicineApplication.getIP()+":"+ NetConfig.port+ File.separator
						 + interfaceName;
			 }


		 }else if(net==NetConfig.MEDIESERVERHeal){
			 url = MedicineApplication.getIP()+":"+ NetConfig.port + File.separator+"health"+ File.separator
					 + interfaceName;
		 }else if(net == NetConfig.SINGIN){
			 url = MedicineApplication.getIP() + File.separator+"sign"+ File.separator
					 + interfaceName;
		 }

		return url;
	}

	public static String getUrlByParams(String p,String interfaceName ) {
		String url = "";

			url = MedicineApplication.getIP()+"" + File.separator+p+ File.separator
					+ interfaceName;


		return url;
	}
	public static String getUrl(String interfaceName, int net,String port) {
		String url = "";

		if(net==NetConfig.HIPPOSERVER){
			url = MedicineApplication.getUrl() + File.separator
					+ interfaceName;
		}else if(net == NetConfig.MEDIESERVER){

				url = MedicineApplication.getIP()+":"+ NetConfig.port + File.separator
						+ interfaceName;

		}else if(net==NetConfig.MEDIESERVERHeal){
			url = MedicineApplication.getIP()+":"+ NetConfig.port + File.separator+"health"+ File.separator
					+ interfaceName;
		}else if(net == NetConfig.SINGIN){
			url = MedicineApplication.getIP() + File.separator+"sign"+ File.separator
					+ interfaceName;
		}

		return url;
	}
	public static String getUrl(Map<String, String> interfaces, int net) {
		// SharedPreferenceUtil.getString("session", "");
		String url = "";
		if (net == NetConfig.HIPPOSERVER) {
			url = MedicineApplication.getUrl() + File.separator
					+ interfaces.get("interface1") + File.separator
					+ MedicineApplication.getSession()
					+ File.separator + interfaces.get("interface2");
		} else if (net == NetConfig.MEDIESERVER) {
			url = MedicineApplication.getIP()+":"+ NetConfig.port+ File.separator
					+ interfaces.get("interface1");
		}else if(net==NetConfig.MEDIESERVERHeal){
			url = MedicineApplication.getIP()+":"+ NetConfig.port + File.separator+"health"+ File.separator
					+ interfaces.get("interface1");
		}
		return url;
	}
	public static String getUrl(Map<String, String> interfaces, int net,String port) {
		// SharedPreferenceUtil.getString("session", "");
		String url = "";
		if (net == NetConfig.HIPPOSERVER) {

			url = MedicineApplication.getUrl() + File.separator
					+ interfaces.get("interface1") + File.separator
					+ MedicineApplication.getMapConfig().get("session")
					+ File.separator + interfaces.get("interface2");
		} else if (net == NetConfig.MEDIESERVER) {
			url = MedicineApplication.getIP()+":"+port+ File.separator
					+ interfaces.get("interface1");
		}
		return url;
	}
}
