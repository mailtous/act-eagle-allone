package com.artlongs.framework.query;

import com.artlongs.framework.utils.Assert;
import com.artlongs.tester.User;
import org.osgl.http.H;
import org.osgl.util.C;
import org.osgl.util.S;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * 
* @ClassName: PropertyFilter 
* @Description: 属性过滤器
* @date 2013-8-12 上午9:16:43
*
 */
public class PropertyFilter {
	private String fieldName;
	private String otherField;
	private MatchType matchType;
	private boolean or;
	private boolean and;
	private boolean roundOr;
	private boolean roundAnd;
	private Object[] values;
	private List<PropertyFilter> filters = new ArrayList<PropertyFilter>();

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private PropertyFilter(final String filterName, final String... value) {
		String firstPart = S.before(filterName, "_");
        String matchTypeCode = S.string(firstPart).substring(0, firstPart.length() - 1);
        String propertyTypeCode = S.string(firstPart).substring(firstPart.length() - 1, firstPart.length() - 1);

        Class<?> propertyClass = null;
        
        try {
        	matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (RuntimeException e) {
                throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
        }

        try {
        	propertyClass = Enum.valueOf(FieldType.class, propertyTypeCode).getValue();
        } catch (RuntimeException e) {
                throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
        }
        
        String propertyName = S.after(filterName, "_");

        Assert.isTrue(S.isNotBlank(propertyName), "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
        Assert.isNull(propertyClass, "filter名称" + filterName + "没有按规则编写,无法得到属性类型.");
        
        this.fieldName = propertyName;
		this.values = (Object[]) value;
        filters.add(this);
	}

	/**
	 * values为具体类型值的构造函数
	 * 
	 * @param fieldName
	 *            属性名
	 * @param matchType
	 *            匹配类型 {@link com.fiidee.eagle.framework.base.dao.util.PropertyFilter.MatchType}
	 * @param values
	 *            值数组，MatchType为BETWEEN类型时，长度必须是2，其他为1，值必须是具体类型的值，
	 *            如果是字符串需要转换类型，见另一个构造函数
	 *            {@link #PropertyFilter(String, com.fiidee.eagle.framework.base.dao.util.PropertyFilter.MatchType, com.fiidee.eagle.framework.base.dao.util.PropertyFilter.FieldType, Object...)}
	 */
	@SuppressWarnings("unchecked")
	public PropertyFilter(final String fieldName, MatchType matchType, Object... values) {
		this.fieldName = fieldName;
		this.matchType = matchType;
		if (this.matchType == MatchType.BETWEEN
				&& (values == null || values.length != 2)) {
			throw new IllegalArgumentException(String.format(
					"%s属性选择MatchType.BETWEEN类型时，values参数长度必须为2", fieldName));
		}
		this.values = values;
		filters.add(this);
	}


	/**
	 * 属性比较构造函数
	 * 
	 * @param fieldName
	 *            属性名
	 * @param matchType
	 *            条件类型
	 * @param otherField
	 *            其他属性
	 */
	public PropertyFilter(final String fieldName, String otherField, MatchType matchType) {
		this.fieldName = fieldName;
		this.matchType = matchType;
		this.otherField = otherField;
		filters.add(this);
	}
	
	public static List<PropertyFilter> buildFromHttpRequest(
			final H.Request request) {
		return buildFromHttpRequest(request, "filter");
	}
	
	public static List<PropertyFilter> buildFromHttpRequest(
			final  H.Request request, final String filterPrefix) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

		// 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = getParametersStartingWith(request, filterPrefix + "_");

		// 分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
			String filterName = entry.getKey();
			String[] values = null;
			if(entry.getValue() instanceof String[]) {
				values = (String[])entry.getValue();
			} else {
				values = new String[] { entry.getValue().toString() };
			}
			// 如果value值为空,则忽略此filter.
			if (values != null && !isArrayAllBlank(values)) {
				PropertyFilter filter = new PropertyFilter(filterName, values);
				filterList.add(filter);
			}
		}
		return filterList;
	}

	public static Map<String, Object> getParametersStartingWith(H.Request request, String prefix) {
		Assert.isNull(request, "Request must not be null");
		if (prefix == null) prefix = "";
		Map<String, Object> params = new TreeMap<String, Object>();
		//
		Iterable<String> paramNames = request.paramNames();
		for (String paramName : paramNames) {
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.paramVals(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	/**
	 * 
	* @Title: isArrayNotBlank 
	* @Description: 判断查询条件中的数组值是否不为空
	* @param @param values
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private static boolean isArrayNotBlank(String[] values) {
		boolean isBlank = true;
		
		for(String value : values) {
			if(S.isBlank(value))
				return false;
		}
		return isBlank;
	}

	/** 判断数组值都为空 */
	private static boolean isArrayAllBlank(String[] values) {
		boolean isAllBlank = true;
		for(String value : values) {
			if(S.isNotBlank(value))
				return false;
		}
		return isAllBlank;
	}


    /**
	 * 获取属性名
	 * 
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 向当前filter添加一个or联合过滤条件
	 * 
	 * @param filter
	 * @return
	 */
	public PropertyFilter addOrFilter(PropertyFilter filter) {
		filter.or = true;
		filters.add(filter);
		return this;
	}

	/**
	 * 向当前filter添加一个or联合过滤条件，
	 * <p>
	 * 过滤条件将作为一个整体,即将所有条件放入括号内
	 * 
	 * @param filter
	 * @return
	 */
	public PropertyFilter addRoundOrFilter(PropertyFilter filter) {
		//Assert.isTrue(filter == this, "PropertyFilter不允许添加自身");
		filter.roundOr = true;
		filters.add(filter);
		return this;
	}

	/**
	 * 向当前filter添加一个and联合过滤条件，
	 * 
	 * @param filter
	 * @return
	 */
	public PropertyFilter addAndFilter(PropertyFilter filter) {
		//Assert.isTrue(filter == this, "PropertyFilter不允许添加自身");
		filter.and = true;
		filters.add(filter);
		return this;
	}

	/**
	 * 
	 * 向当前filter添加一个and联合过滤条件，
	 * <p>
	 * 过滤条件将作为一个整体,即将所有条件放入括号内
	 * 
	 * @param filter
	 * @return
	 */
	public PropertyFilter addRoundAndFilter(PropertyFilter filter) {
		//Assert.isTrue(filter == this, "PropertyFilter不允许添加自身");
		filter.roundAnd = true;
		filters.add(filter);
		return this;
	}

	/**
	 * 判断该filter是否是一个or联合过滤，见{@link #addOrFilter(PropertyFilter)}
	 * 
	 * @return
	 */
	public boolean isOr() {
		return or;
	}

	/**
	 * 判断该filter是否是一个and联合过滤，见{@link #addAndFilter(PropertyFilter)}
	 * 
	 * @return
	 */
	public boolean isAnd() {
		return and;
	}

	/**
	 * 判断该filter是否是一个or联合过滤, 见 {@link #addRoundOrFilter(PropertyFilter)}
	 * 
	 * @return
	 */
	public boolean isRoundOr() {
		return roundOr;
	}

	/**
	 * 判断该filter是否是一个and联合过滤, 见 {@link #addRoundAndFilter(PropertyFilter)}
	 * 
	 * @return
	 */
	public boolean isRoundAnd() {
		return roundAnd;
	}
	/**
	 * 判断该filter是否是一个联合过滤
	 * @return
	 */
	public boolean isMulti() {
		return !filters.isEmpty() && filters.size()>1;
	}
	/**
	 * 获取属性的比较类型
	 * @return
	 */
	public MatchType getMatchType() {
		return matchType;
	}
	/**
	 * 获取属性比较参数值集合
	 * @return
	 */
	public Object[] getValues() {
		return values;
	}

	/**
	 * 联合filter迭代器
	 * <p>
	 * 不支持删除操作
	 * 
	 * @return
	 */
	public Iterator<PropertyFilter> iterator() {
		return new Iterator<PropertyFilter>() {
			private Iterator<PropertyFilter> it = filters.iterator();

			public boolean hasNext() {
				return it.hasNext();
			}

			public PropertyFilter next() {
				return it.next();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}

			public void forEachRemaining(Consumer<? super PropertyFilter> action) {}
		};
	}

	/**
	 * 联合filter作为一个过滤条件
	 * 
	 * @param filter
	 * @return
	 */
	public PropertyFilter joinFilter(PropertyFilter filter) {
		Assert.isTrue(filter == this, "PropertyFilter不允许添加自身");
		filters.add(filter);
		return this;
	}
	/**
	 * 其他field，两个属性比较时
	 * 
	 * @return
	 */
	public String getOtherField() {
		return otherField;
	}
	/**
	 * 属性类型
	 * @author sun4love
	 *
	 */
	public enum FieldType {
		S(String.class), 
		D(Date.class), 
		I(Integer.class), 
		N(Double.class), 
		L(Long.class), 
		B(Boolean.class);
		
		private Class<?> clazz;

		private FieldType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	/** 属性比较类型. */
	public enum MatchType {
		/**
		 * 等于
		 * */
		EQ,

		/**
		 * 等于另一属性
		 * */
		EQF,

		/**
		 * like 'value'
		 * */
		LIKE,

		/**
		 * like '%value'
		 * */
		LIKESTART,

		/**
		 * like 'value%'
		 * */
		LIKEEND,

		/**
		 * like '%value%'
		 * */
		LIKEANYWHERE,
		
		/**
		 * 针对整型字段like '%value'
		 */
		LIKEISTART,
		
		/**
		 * 针对整型字段like 'value%'
		 */
		LIKEIEND,
		
		/**
		 * 针对整型字段like '%value%'
		 */
		LIKEIANYWHERE,

		/**
		 * 小于
		 * */
		LT,

		/**
		 * 小于另一属性
		 * */
		LTF,

		/**
		 * 大于
		 * */
		GT,

		/**
		 * 大于另一属性
		 * */
		GTF,

		/**
		 * 小于等于
		 * */
		LE,

		/**
		 * 小于等于另一属性
		 * */
		LEF,

		/**
		 * 大于等于
		 */
		GE,

		/**
		 * 大于等于另一属性
		 */
		GEF,

		/**
		 * 
		 * 在两者之间
		 * 
		 */
		BETWEEN,

		/**
		 * 
		 * 不等于
		 * 
		 */
		NE,

		/**
		 * 
		 * 不等于另一属性
		 * 
		 */
		NEF,

		/**
		 * 包含
		 */
		IN ,
        /**
         * 为空
         */
        ISNULL,
		/**
		 * 不为空
		 */
		NOTNULL
	}

	public List<PropertyFilter> getFilters() {
		return filters;
	}

    /**
     * 按指定条件删除 PropertyFilter 项
     * @param filterList
     * @param filterName 指定的filterName
     * @param filterVal 指定的值
     */
    public static void remove(List<PropertyFilter> filterList,String filterName,Object filterVal){
        if (null != filterList && filterList.size() > 0) {
            Iterator<PropertyFilter> iterator = filterList.iterator();
            while (iterator.hasNext()) {
                PropertyFilter pf = iterator.next();
                if (pf.getFieldName().equals(filterName)) {
                    for (Object o : pf.getValues()) {
                        if (o.equals(filterVal)) {
                            iterator.remove();
                        }
                    }
                }

            }
        }
    }

	public static void main(String[] args) {
		List<PropertyFilter> filterList = C.newList();
		filterList.add(new PropertyFilter("auditStatus", PropertyFilter.MatchType.EQ, 1));
		String sql = new Lq<User>(User.class).whereOfFilters(filterList).build();
		System.err.println(sql);

	}

}
