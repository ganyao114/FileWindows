package com.gy.filewindows.downloader;

/**********
 *  @author   YinJie
 *  <Type Create time>     2013��11��14��  ��    ����1:13:16
 *  TODO  >>  ���ؽ��ȼ�����
 * ****************************************************************************************
 */
public interface DownloadProgressListener {

	/*****
	 *  < Method >  ���ؽ��ȼ�����������ȡ���������ص����ݵĴ�С
	 *  @param downloadSize ���ݴ�С
     *****************************************************************************************
	 */
	void onDownloadSize(int downloadSize);
}
