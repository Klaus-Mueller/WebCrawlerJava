package com.axreng.backend.test;
import org.junit.jupiter.api.Test;

import com.axreng.backend.WebCrawler;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebCrawlerTest {

	@Test
	public void testExtractLinks() {
		String pageContent = "	prepare a new version of this manual page compilation.</p></div></div></div><div class=\"navfooter\"><hr /><table width=\"100%\" summary=\"Navigation footer\"><tr><td width=\"40%\" align=\"left\"> </td><td width=\"20%\" align=\"center\"> </td><td width=\"40%\" align=\"right\"> <a accesskey=\"n\" href=\"manpageindex.html\">Next</a></td></tr><tr><td width=\"40%\" align=\"left\" valign=\"top\"> </td><td width=\"20%\" align=\"center\"> </td><td width=\"40%\" align=\"right\" valign=\"top\"> Manual page section index</td></tr></table></div></body></html>";

		WebCrawler webCrawler = new WebCrawler("http://example.com");
		List<String> links = webCrawler.extractLinks(pageContent);

		assertEquals(1, links.size());
		assertEquals("http://example.com/manpageindex.html", links.get(0));
	}
}
