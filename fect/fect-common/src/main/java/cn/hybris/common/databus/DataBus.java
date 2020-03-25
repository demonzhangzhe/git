package cn.hybris.common.databus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.google.gson.GsonBuilder;

import cn.hybris.common.util.IOUtils;

/**
 * 
 * 数据存取及传输的工具:数据总线，用扁平化的数据结构存储数据
 * 
 * @author Qu Dihuai
 *
 */
public class DataBus implements Serializable
{
	private static final Logger log = LoggerFactory.getLogger(DataBus.class);

	private static final long serialVersionUID = 4084741653120544931L;

	private final Map<String, Object> data = new ConcurrentHashMap<>(32);

	/**
	 * 在总线对象里面存入数据
	 * 
	 * @param key
	 * @param value
	 */
	public <T> void set(final String key, final T value)
	{
		data.put(key, value);
	}

	/**
	 * 在总线对象里面获取数据
	 * 
	 * @param key
	 * @param value
	 * @return <V> V
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key)
	{
		return (T) data.get(key);
	}

	@Override
	public String toString()
	{
		return new GsonBuilder().disableHtmlEscaping().create().toJson(this);
	}

	/**
	 * 深度克隆总线对象
	 * 
	 * @return DataBus
	 */
	@Override
	public DataBus clone() throws CloneNotSupportedException
	{
		ByteArrayOutputStream baos = null;
		ByteArrayInputStream bais = null;

		try
		{
			baos = new ByteArrayOutputStream();
			final HessianOutput output = new HessianOutput(baos);
			output.writeObject(this);

			final byte[] bytes = baos.toByteArray();
			bais = new ByteArrayInputStream(bytes);

			final HessianInput input = new HessianInput(bais);
			return (DataBus) input.readObject();
		} catch (final IOException e) {
			log.error("Clone not support", e);
			throw new CloneNotSupportedException();
		} finally {
			IOUtils.close(baos);
			IOUtils.close(bais);
		}
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof DataBus)
		{
			return this.toString().equals(((DataBus) obj).toString());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	/**
	 * 把总线对象的序列化字符串转化为总线对象
	 * 
	 * @param json
	 * @return DataBus
	 */
	public static DataBus fromString(final String json)
	{
		return new GsonBuilder().disableHtmlEscaping().create().fromJson(json, DataBus.class);
	}
}
