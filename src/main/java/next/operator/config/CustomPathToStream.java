package next.operator.config;

import org.ansj.dic.PathToStream;

import java.io.InputStream;

/** 自定義字典檔讀取方式 @see PathToStream */
public class CustomPathToStream extends PathToStream {

  @Override
  public InputStream toStream(String s) {
    return this.getClass().getResourceAsStream("/" + s.split("\\|")[1]);
  }

}
