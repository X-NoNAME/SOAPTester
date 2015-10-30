/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.x_noname.test;

import java.net.URL;
import javax.xml.namespace.QName;
import net.yandex.speller.services.spellservice.*;
import org.assertj.SoftAssertions;
import org.testng.*;
import org.testng.annotations.*;

/**�������� �����
 *
 * @author Ivan (X-NoNAME) Kazakov
 * @mailto mail@x-noname.ru
 */
@Listeners(SOAPTest.Listener.class)
public class SOAPTest {

    @Test(description = "���������� ����")
    public void t1_positive() throws Exception {
        CheckTextRequest r = new CheckTextRequest();
        r.setText("����� ��� ������");
        CheckTextResponse resp = port.checkText(r);
        SpellResult res = resp.getSpellResult();
        soft.assertThat(res).as("�������� SpellResult").hasNoError();
    }

    @Test(description = "���������� ����")
    public void t2_negative() throws Exception {
        CheckTextRequest r = new CheckTextRequest();
        r.setText("����� � ������� � ����� �������");
        CheckTextResponse resp = port.checkText(r);
        SpellResult res = resp.getSpellResult();
        soft.assertThat(res.getError().get(0)).as("�������� SpellError")
                .hasCode(1)
                .hasCol(24) 
                .hasLen(7)
                .hasPos(24)
                .hasRow(0)
                .hasWord("�������")
                .hasOnlyS("�������");
    }
    
    private static final QName SERVICE_NAME = new QName("http://speller.yandex.net/services/spellservice", "SpellService");
    private SpellServiceSoap port;
    private SoftAssertions soft;
    
    //����������� ����� ������� ��� ������
    @BeforeClass
    public void setUp() throws Exception {
        SpellService ss = new SpellService(new URL("http://speller.yandex.net/services/spellservice?WSDL"), SERVICE_NAME);
        port = ss.getSpellServiceSoap();
    }

    //����� ����������� ����� ������� �������� �������
    @BeforeMethod
    public void before() throws Exception {
        soft = new SoftAssertions();
    }

    //�������� ����� ��� ����, ��� �� ��������� SoftAssertion � �������� �������
    public static class Listener implements IInvokedMethodListener2 {
        public void afterInvocation(IInvokedMethod iim, ITestResult itr, ITestContext itc) {
            if (iim.isTestMethod()) {
                ((SOAPTest) iim.getTestMethod().getInstance()).soft.assertAll();
            }
        }
        public void beforeInvocation(IInvokedMethod iim, ITestResult itr, ITestContext itc) {}
        public void beforeInvocation(IInvokedMethod iim, ITestResult itr) {}
        public void afterInvocation(IInvokedMethod iim, ITestResult itr) {}
    }

}
