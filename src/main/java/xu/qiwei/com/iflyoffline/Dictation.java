package xu.qiwei.com.iflyoffline;


import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.List;

import xu.qiwei.com.iflyoffline.utils.ApkInstaller;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dictation implements RecognizerDialogListener {
    private static Dictation instance;
    private StringBuffer sb;
    private Context context;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_LOCAL;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    private Toast mToast;
    // 语记安装助手类
    private ApkInstaller mInstaller;
    //    听写回调
    private DictionResult dictionResult;
//    private RecognizerDialogListener recognizerDialogListener;

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
//            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }else
            {
                Log.e("iflylog==","执行赋值操作");
                setParamV2();
            }
        }
    };

    public static Dictation getInstance() {
        if (instance == null) {
            synchronized (Dictation.class) {
                if (instance == null) {
                    instance = new Dictation();
                }
            }
        }
        return instance;
    }

    public Dictation init(Context context) {
        this.context = context;
        this.sb = new StringBuffer();
        initEnvironMent();
        return this;
    }

    private Dictation() {
        // Required empty public constructor

    }

    public void startDictation(DictionResult dictionResult) {
        this.dictionResult = dictionResult;
//        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        mIatDialog.show();
    }

    private void initEnvironMent() {
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        mIatDialog = new RecognizerDialog(context, mInitListener);
        mIatDialog.setListener(this);
        mInstaller = new ApkInstaller((FragmentActivity) context);
        Log.e("iflylog==","初始化完成");

    }


    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private void setParamV2(){
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        String lag = "mandarin";
        // 设置引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        }else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT,lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }
    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = "mandarin";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean b) {
        Log.e("dictationresult=", recognizerResult.getResultString());
        DictionData dictionData = new Gson().fromJson(recognizerResult.getResultString(), DictionData.class);
        List<DictionData.WsBean> wsBeanList = dictionData.getWs();
        if (wsBeanList != null && wsBeanList.size() != 0) {
            List<DictionData.WsBean.CwBean> cwBeanList = wsBeanList.get(0).getCw();
            if (cwBeanList!=null&&cwBeanList.size()!=0) {
                if (dictionResult != null) {
                    sb.append(cwBeanList.get(0).getW());
                    if (dictionData.isLs())
                    {
                        dictionResult.onDictionSuccess(sb.toString());
                        sb = new StringBuffer();
                    }

                } else {
                    Log.e("iflylog=", "未找到回调接口");
                }
            }


        }

    }

    @Override
    public void onError(SpeechError speechError) {
        if (dictionResult!=null) {
            dictionResult.onDictionFailure(speechError);
        }
    }

    public interface DictionResult {
        void onDictionSuccess(String resultStr);

        void onDictionFailure(SpeechError var1);
    }
}
