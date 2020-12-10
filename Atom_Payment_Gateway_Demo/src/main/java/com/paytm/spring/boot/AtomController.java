package com.paytm.spring.boot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import com.atom.entity.AtomWebTransactions;
import com.atom.enums.PaymentType;
import com.atom.utill.CommonUtil;

@Controller
public class AtomController {
	
	public static String ATOM_PAYMENTPAGE = "";

	@RequestMapping(value = "/atomPayment", method = RequestMethod.GET)
	public ModelAndView atomPayment() {
		
		return new ModelAndView("index.jsp");
	}
	
	@RequestMapping(value = "/atom-payment", method = RequestMethod.POST)
    public ModelAndView userLogsForAdmin(final HttpSession session,
                                         final RedirectAttributes redirectAttributes) throws IOException, ParserConfigurationException, SAXException {

		Map<String,Object> map = new HashMap<>();

        //Payment Integration
        //PrintWriter out = new PrintWriter("hello");
        //map.put("out",out);
        URLConnection con;
        URL url = null;
        String vVenderURL;
        Double totalPayment = 100d;
        String vOlnAmt = totalPayment+"0";
        String vOlnTxnNo = CommonUtil.getAtomTransactionString();
        //String vOlnAmt = totalPayment.toString();
        String vOlnPayMode = "ONLINE";
        String reqHashKey = "KEY123657234";
        String ttype = "NBFundTransfer";
        String txncurr = "INR";
        String signature = CommonUtil.AtomSignatureGenerate(reqHashKey,"197","Test@123",ttype,"NSE",vOlnTxnNo,vOlnAmt,txncurr);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String CurrDateTime = sdf.format(new Date()).toString();
        System.out.println("Signature :- "+signature);
        //  Test URL
        vVenderURL = "https://paynetzuat.atomtech.in/paynetz/epi/fts?login=197&pass=Test@123&ttype="+ttype+"&prodid=NSE"
                + "&amt="+vOlnAmt+"&txncurr="+txncurr+"&txnscamt=0&clientcode=007&txnid="+vOlnTxnNo
                + "&date="+CurrDateTime+"&custacc=100000036600&&ru=http://localhost:8080/atom-paymentws&signature="+signature;

        //vVenderURL = "https://paynetzuat.atomtech.in/paynetz/epi/fts?login=197&pass=Test@123&ttype=NBFundTransfer&prodid=NSE&amt=200.00&txncurr=INR&txnscamt=0&clientcode=001&txnid=M123&date=23/08/2010%2011:57:00&custacc=100000036600&udf1=RakeshMaharana&udf9=fromdate=12-09-2016&ru=https://localhost:8080/atom-paymentws&udf2=vartak.pritish@gmail.com&udf3=8149585158&signature=704e6a78ca61c89127ca5430ddf59a329dacb142ae2790e19d676f5ca8ca80b9c534552e877bfb0ce1c2dddf252ae3d7580d329556213811f828711e9f4ea371";


        vVenderURL = vVenderURL.replace(" ","%20");
        System.out.println(vVenderURL);
        ATOM_PAYMENTPAGE = vVenderURL;
        return new ModelAndView("redirect:" + ATOM_PAYMENTPAGE);

    }
	
	@RequestMapping(value = "/atom-paymentws", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("command") AtomWebTransactions atomWebTransactions,final HttpSession session,final RedirectAttributes redirectAttributes,
                                                        @QueryParam("date") final String date,
                                                        @QueryParam("surcharge") final String surcharge,
                                                        @QueryParam("CardNumber") final String CardNumber,
                                                        @QueryParam("prod") final String prod,
                                                        @QueryParam("clientcode") final String clientcode,
                                                        @QueryParam("mmp_txn") final String mmp_txn,
                                                        @QueryParam("amt") final String amt,
                                                        @QueryParam("udf3") final String udf3,  // Mobile Number
                                                        @QueryParam("merchant_id") final String merchant_id,
                                                        @QueryParam("udf1") final String udf1,  //Name
                                                        @QueryParam("udf2") final String udf2,   //emailId
                                                        @QueryParam("udf4") final String udf4,  //Address
                                                       // @QueryParam("udf5") final String udf5,  
                                                        @QueryParam("udf9") final String udf9,  
                                                        @QueryParam("auth_code") final String auth_code,
                                                        @QueryParam("discriminator") final String discriminator,    //Type of Payment NB- Net Banking,CC-Credit Card, DC- Debit Card, IM-IMPS, MX -American Express Cards
                                                        @QueryParam("mer_txn") final String mer_txn,
                                                        @QueryParam("f_code") final String f_code,
                                                        @QueryParam("bank_txn") final String bank_txn,
                                                        @QueryParam("ipg_txn_id") final String ipg_txn_id,
                                                        @QueryParam("bank_name") final String bank_name,
                                                        @QueryParam("desc") final String desc,   //payment status
                                                        @QueryParam("signature") final String signature
                                                        ) throws IOException {
		
		 Map<String,Object> map = new HashMap<>();
	        String respHashKey = "KEYRESP123657234";
	        String signature_response = CommonUtil.AtomSignatureGenerate(respHashKey, mmp_txn,mer_txn, f_code,prod, discriminator, amt, bank_txn);
	        if (signature.equals(signature_response)) {
	            InetAddress ipAddress = InetAddress.getLocalHost();

	            if (atomWebTransactions.getStatus().equals("Transction Success")) {
	                String paymentMode;
	                if (PaymentType.NET_BANKING.getValue().equals(atomWebTransactions.getDiscriminator())){
	                    paymentMode = "Net Banking";
	                }else if(PaymentType.CREDIT_CARD.getValue().equals(atomWebTransactions.getDiscriminator())){
	                    paymentMode = "Credit Card";
	                }else if(PaymentType.DEBIT_CARD.getValue().equals(atomWebTransactions.getDiscriminator())){
	                    paymentMode = "Debit Card";
	                }else{
	                    paymentMode = "American Express";
	                }
	            /*redirectAttributes.addFlashAttribute(Constants.MESSAGE,
	                    messageConfig.getMessage("msg.atomtenantpymt-1"));*/
	                if (atomWebTransactions != null) {
	                    map.put("merchantName", "abc");
	                    map.put("merchantSite", "beepcabs.com");
	                    map.put("transactionReferenceNo", atomWebTransactions.getOnlineReferenceNumber());
	                    map.put("atomTransactionNo", atomWebTransactions.getIpg_txn_id());
	                    map.put("paymentMode", paymentMode);
	                    map.put("amount", atomWebTransactions.getAmount());
	                    map.put("customerName", atomWebTransactions.getMerchantName());
	                    map.put("emailId", atomWebTransactions.getEmailId());
	                    return new ModelAndView("success.jsp", map);
	                }

	            } else {
	                return new ModelAndView("failure.jsp");
	            /*redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE,
	                    messageConfig.getMessage("msg.atomtenantpymt-2"));*/
	            }

	        }else {
	            return new ModelAndView("wrong_signature.jsp");
	        }
			return new ModelAndView("failure.jsp");
}
}
