package cn.wuxia.project.common.third.iflytek;

import com.iflytek.cloud.speech.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MscTest {
	private static final Logger logger = LoggerFactory.getLogger(MscTest.class);
	private static final String APPID = "580dc2c1";

	private static final String USER_WORDS = "{\"userword\":[{\"name\":\"计算机词汇\",\"words\":[\"随机存储器\",\"只读存储器\",\"扩充数据输出\",\"局部总线\",\"压缩光盘\",\"十七寸显示器\"]},{\"name\":\"我的词汇\",\"words\":[\"槐花树老街\",\"王小贰\",\"发炎\",\"公事\"]}]}";

	private static MscTest mObject;

	private static StringBuffer mResult = new StringBuffer();
	
	private boolean mIsLoop = true;

	public static void main(String args[]) {
		if( null!=args && args.length>0 && args[0].equals("true") ){
			//在应用发布版本中，请勿显示日志，详情见此函数说明。
			Setting.setShowLog( true );
		}
		System.out.println(System.getProperty("java.library.path"));
		SpeechUtility.createUtility("appid=" + APPID);
//		getMscObj().Synthesize();
		getMscObj().Recognize();
	}

	private static MscTest getMscObj() {
		if (mObject == null)
			mObject = new MscTest();
		return mObject;
	}


	// *************************************音频流听写*************************************

	/**
	 * 听写
	 */
	
	private boolean mIsEndOfSpeech = false;
	private void Recognize() {
		if (SpeechRecognizer.getRecognizer() == null)
			SpeechRecognizer.createRecognizer();
		mIsEndOfSpeech = false;
		RecognizePcmfileByte();
	}

	/**
	 * 自动化测试注意要点 如果直接从音频文件识别，需要模拟真实的音速，防止音频队列的堵塞
	 */
	public void RecognizePcmfileByte() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
		//写音频流时，文件是应用层已有的，不必再保存
//		recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//				"./iat_test.pcm");
		recognizer.setParameter( SpeechConstant.RESULT_TYPE, "plain" );
		recognizer.startListening(recListener);
		
		FileInputStream fis = null;
		final byte[] buffer = new byte[64*1024];
		try {
			fis = new FileInputStream(new File("/app/logs/ZRJy5.amr"));
			if (0 == fis.available()) {
				mResult.append("no audio avaible!");
				recognizer.cancel();
			} else {
				int lenRead = buffer.length;
				while( buffer.length==lenRead && !mIsEndOfSpeech ){
					lenRead = fis.read( buffer );
					recognizer.writeAudio( buffer, 0, lenRead );
				}//end of while
				
				recognizer.stopListening();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis) {
					fis.close();
					fis = null;
				}
		} catch (IOException e) {
				e.printStackTrace();
			}
		}//end of try-catch-finally
		
	}

	/**
	 * 听写监听器
	 */
	private RecognizerListener recListener = new RecognizerListener() {

		public void onBeginOfSpeech() {
			logger.debug( "onBeginOfSpeech enter" );
			logger.debug("*************开始录音*************");
		}

		public void onEndOfSpeech() {
			logger.debug( "onEndOfSpeech enter" );
			mIsEndOfSpeech = true;
		}

		public void onVolumeChanged(int volume) {
			logger.debug( "onVolumeChanged enter" );
			if (volume > 0)
				logger.debug("*************音量值:" + volume + "*************");

		}

		public void onResult(RecognizerResult result, boolean islast) {
			logger.debug( "onResult enter" );
			mResult.append(result.getResultString());
			
			if( islast ){
				logger.debug("识别结果为:" + mResult.toString());
				mIsEndOfSpeech = true;
				mResult.delete(0, mResult.length());
				waitupLoop();
			}
		}

		public void onError(SpeechError error) {
			mIsEndOfSpeech = true;
			logger.debug("*************" + error.getErrorCode()
					+ "*************");
			waitupLoop();
		}

		public void onEvent(int eventType, int arg1, int agr2, String msg) {
			logger.debug( "onEvent enter" );
		}

	};

	// *************************************无声合成*************************************

	/**
	 * 合成
	 */
	private void Synthesize() {
		SpeechSynthesizer speechSynthesizer = SpeechSynthesizer
				.createSynthesizer();
		// 设置发音人
		speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		//设置语速
		speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
		 //设置音量，范围 0~100
		 speechSynthesizer.setParameter(SpeechConstant.VOLUME, "80");
		// 设置合成音频保存位置（可自定义保存位置），默认不保存
		speechSynthesizer.synthesizeToUri("语音合成测试程序 ", "/app/tts_test.pcm",
				synthesizeToUriListener);
	}

	/**
	 * 合成监听器
	 */
	SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {

		public void onBufferProgress(int progress) {
			logger.debug("*************合成进度*************" + progress);

		}

		public void onSynthesizeCompleted(String uri, SpeechError error) {
			if (error == null) {
				logger.debug("*************合成成功*************");
				logger.debug("合成音频生成路径：" + uri);
			} else
				logger.debug("*************" + error.getErrorCode()
						+ "*************");
			waitupLoop();

		}



	};

	// *************************************词表上传*************************************

	/**
	 * 词表上传
	 */
	private void uploadUserWords() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		if ( recognizer == null) {
			recognizer = SpeechRecognizer.createRecognizer();
			
			if( null == recognizer ){
				logger.debug( "获取识别实例实败！" );
				waitupLoop();
				return;
			}
		}

		UserWords userwords = new UserWords(USER_WORDS);
		recognizer.setParameter( SpeechConstant.DATA_TYPE, "userword" );
		recognizer.updateLexicon("userwords",
				userwords.toString(), 
				lexiconListener);
	}

	/**
	 * 词表上传监听器
	 */
	LexiconListener lexiconListener = new LexiconListener() {
		@Override
		public void onLexiconUpdated(String lexiconId, SpeechError error) {
			if (error == null)
				logger.debug("*************上传成功*************");
			else
				logger.debug("*************" + error.getErrorCode()
						+ "*************");
			waitupLoop();
		}

	};

	private void waitupLoop(){
		synchronized(this){
			MscTest.this.notify();
		}
	}


}
