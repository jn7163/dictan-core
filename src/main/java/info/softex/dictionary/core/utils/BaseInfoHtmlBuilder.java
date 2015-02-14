/*
 *  Dictan Open Dictionary Java Library presents the core interface and functionality for dictionaries. 
 *	
 *  Copyright (C) 2010 - 2015  Dmitry Viktorov <dmitry.viktorov@softex.info> <http://www.softex.info>
 *	
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License (LGPL) as 
 *  published by the Free Software Foundation, either version 3 of the License, 
 *  or any later version.
 *	
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *	
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package info.softex.dictionary.core.utils;

import info.softex.dictionary.core.attributes.BasePropertiesInfo;
import info.softex.dictionary.core.attributes.LanguageDirectionsInfo;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 
 * @since version 2.0, 03/13/2011
 * 
 * @modified version 2.6, 09/17/2011
 * @modified version 3.7, 06/14/2013
 * @modified version 3.9, 12/05/2013
 * @modified version 4.0, 02/09/2014
 * @modified version 4.6, 02/08/2015
 *  
 * @author Dmitry Viktorov
 * 
 */
public class BaseInfoHtmlBuilder {
	
	protected final static DecimalFormat FORMATTER_SIZE = new DecimalFormat("#.##");
	protected final static DecimalFormat FORMATTER_NUMBER = new DecimalFormat("#,###");
	
	protected enum StringKey {
		FORMAT_VERSION,
		DICTIONARY_BASE,
		COMP_DATE,
		COMPILED_BY,
		BASE_VERSION,
		BASE_SIZE,
		BASE_PARTS_NUMBER,
		NUMBER_OF_WORDS,
		NUMBER_OF_ARTICLES,
		NUMBER_OF_REDIRECTS,
		NUMBER_OF_ABBREVIATIONS,
		NUMBER_OF_RESOURCES,
		ARTICLES_FORMATTING,
		ARTICLES_FORMATTING_INJECT_WORDS,
		ABBREV_FORMATTING,		
		FILE_SIZE,
		MEDIA_BASE,
		CODEPAGE,
		LANGUAGES
		;
		
		@SuppressWarnings("serial")
		protected static HashMap<StringKey, String> enStrings = new HashMap<StringKey, String>() {{
			put(FORMAT_VERSION, "Ver.");
			put(DICTIONARY_BASE, "Dictionary Base");
			put(MEDIA_BASE, "Media Base");
			put(COMP_DATE, "Compilation Date");
			put(COMPILED_BY, "Compiled By");
			put(BASE_VERSION, "Version");
			put(BASE_SIZE, "Base Size");
			put(BASE_PARTS_NUMBER, "Number of Base Parts");
			put(NUMBER_OF_WORDS, "Number of Words");
			put(NUMBER_OF_ARTICLES, "Number of Articles");
			put(NUMBER_OF_REDIRECTS, "Number of Redirects");
			put(NUMBER_OF_ABBREVIATIONS, "Number of Abbreviations");
			put(NUMBER_OF_RESOURCES, "Number of Resources");
			put(ARTICLES_FORMATTING, "Articles Formatting");
			put(ARTICLES_FORMATTING_INJECT_WORDS, "Prefix Articles with Words");
			put(ABBREV_FORMATTING, "Abbrev. Formatting");	
			put(CODEPAGE, "Codepage");
			put(LANGUAGES, "Languages");
		}};
		
		@SuppressWarnings("serial")
		protected static HashMap<StringKey, String> ruStrings = new HashMap<StringKey, String>() {{
			put(FORMAT_VERSION, "Ver.");
			put(DICTIONARY_BASE, "Словарная База");
			put(MEDIA_BASE, "Медиа База");
			put(COMP_DATE, "Дата Сборки");
			put(COMPILED_BY, "Автор Сборки");
			put(BASE_VERSION, "Версия");
			put(BASE_SIZE, "Размер");
			put(BASE_PARTS_NUMBER, "Количество Частей Базы");
			put(NUMBER_OF_ABBREVIATIONS, "Количество Сокращений");
			put(NUMBER_OF_WORDS, "Количество Слов");
			put(NUMBER_OF_ARTICLES, "Количество Статей");
			put(NUMBER_OF_REDIRECTS, "Количество Редиректов");
			put(NUMBER_OF_RESOURCES, "Количество Ресурсов");
			put(ARTICLES_FORMATTING, "Формат Статей");
			put(ARTICLES_FORMATTING_INJECT_WORDS, "Префикс Статей Словами");
			put(ABBREV_FORMATTING, "Формат Сокращений");
			put(CODEPAGE, "Кодовая Страница");
			put(LANGUAGES, "Языки");
		}};
	}
	
	protected static String getString(StringKey key, String lang) {
		String res = null;
		if (lang != null && lang.equalsIgnoreCase("ru")) {
			res = StringKey.ruStrings.get(key);
		}	
		if (res == null) {
			res = StringKey.enStrings.get(key);
		}
		return res;
	}
	
	public static String buildDictionaryInfoTable(BasePropertiesInfo baseInfo, LanguageDirectionsInfo langsInfo, String lang) {
		
		String compDate = wrapRow(getString(StringKey.COMP_DATE, lang), getShortDate(baseInfo.getCompilationDate()), true);
		
		String combinedVersion = getShortDate(baseInfo.getBaseDate());
		if (combinedVersion == null) {
			combinedVersion = baseInfo.getBaseVersion();
		} else if (baseInfo.getBaseVersion() != null && baseInfo.getBaseVersion().length() != 0) {
			combinedVersion += " (" + baseInfo.getBaseVersion() + ")";
		}
		
		String baseVersion = wrapRow(getString(StringKey.BASE_VERSION, lang), combinedVersion, true);

		String baseSize = getString(StringKey.BASE_SIZE, lang) + ": <dvl>" + formatSize(baseInfo.getBaseFileSize()) + "</dvl>";
		String basePartsNumber = wrapRow(getString(StringKey.BASE_PARTS_NUMBER, lang), baseInfo.getBasePartsTotalNumber(), baseInfo.getBasePartsTotalNumber() > 1);
		
		String wordsNumber = getString(StringKey.NUMBER_OF_WORDS, lang) + ": <dvl>" + formatNumber(baseInfo.getWordsNumber()) + "</dvl>";
		
		String artFormat = wrapRow(getString(StringKey.ARTICLES_FORMATTING, lang), baseInfo.getArticlesFormattingMode(), true);
		String artFormatIWM = wrapRow(getString(StringKey.ARTICLES_FORMATTING_INJECT_WORDS, lang), baseInfo.getArticlesFormattingInjectWordMode(), true);
		String abbrFormat = wrapRow(getString(StringKey.ABBREV_FORMATTING, lang), baseInfo.getAbbreviationsFormattingMode(), baseInfo.getAbbreviationsNumber() > 0);
		
		String createdBy = wrapRow(getString(StringKey.COMPILED_BY, lang), baseInfo.getCompilationCreatorName(), true);

		String langDirections = "";
		if (langsInfo != null) {
			String langs = langsInfo.toLanguagePairsString(true);
			if (langs.length() != 0) {
				langDirections = "<tr><td>" + getString(StringKey.LANGUAGES, lang) + ": <dvl>" + langs + "</dvl></td></tr>";
			}
		}
		
		String codePage = "";
		if (baseInfo.getWordsCodepageName() != null) {
			if (baseInfo.getWordsCodepageName().equalsIgnoreCase(baseInfo.getArticleCodepageName())) {
				codePage = "<tr><td>" + getString(StringKey.CODEPAGE, lang) + ": <dvl>" + 
					formatCPString(baseInfo.getWordsCodepageName()) + "</dvl></td></tr>";
			} else {
				codePage = "<tr><td>" + getString(StringKey.CODEPAGE, lang) + ": <dvl>" + 
					formatCPString(baseInfo.getWordsCodepageName()) + 
					" / " + formatCPString(baseInfo.getArticleCodepageName()) + "</dvl></td></tr>";
			}
		}
		
		String media = "";
		if (baseInfo.isMediaBaseSeparate()) {
			if (baseInfo.getMediaResourcesNumber() != 0) {
				String resNumber = getString(StringKey.NUMBER_OF_RESOURCES, lang) + ": <dvl>" + formatNumber(baseInfo.getMediaResourcesNumber()) + "</dvl>";
				//String mediaFormat = getString(StringKey.FORMAT_VERSION, lang) + ": <dvl>" + baseInfo.getMediaFormatVersion() + "</dvl>";
				String mediaFileSize = getString(StringKey.BASE_SIZE, lang) + ": <dvl>" + formatSize(baseInfo.getBaseFileSize()) + "</dvl>";
				
				media = "<tr><th class=\"subHeader1\">" + getString(StringKey.MEDIA_BASE, lang) + ": " + baseInfo.getMediaFormatName() + " (" + getString(StringKey.FORMAT_VERSION, lang) + " " + baseInfo.getMediaFormatVersion() + ")</th></tr>";
				media += "<tr><td>" + resNumber + "</td></tr>";
				//media += "<tr><td>" + mediaFormat + "</td></tr>";
				media += "<tr><td>" + mediaFileSize + "</td></tr>";
			} else {
				media = "<tr><th class=\"subHeader1\">" + getString(StringKey.MEDIA_BASE, lang) + ": N/A</th></tr>";
			}
		}
		
		String html = "<table>" + 
			"<tr><th class=\"subHeader1\">" + getString(StringKey.DICTIONARY_BASE, lang) + ": " + baseInfo.getFormatName() + " (" + getString(StringKey.FORMAT_VERSION, lang) + " " + baseInfo.getFormatVersion() + ")</th></tr>" +
			"<tr><td>" + wordsNumber + "</td></tr>";
		
		if (baseInfo.getArticlesActualNumber() > 0 && baseInfo.getArticlesActualNumber() != baseInfo.getWordsNumber()) {
			String articlesNumber = getString(StringKey.NUMBER_OF_ARTICLES, lang) + ": <dvl>" + formatNumber(baseInfo.getArticlesActualNumber()) + "</dvl>";
			html += "<tr><td>" + articlesNumber + "</td></tr>";
		}
		
		if (baseInfo.getWordsRelationsNumber() > 0) {
			String redirectsNumber = getString(StringKey.NUMBER_OF_REDIRECTS, lang) + ": <dvl>" + formatNumber(baseInfo.getWordsRelationsNumber()) + "</dvl>";
			html += "<tr><td>" + redirectsNumber + "</td></tr>";
		}
		
		if (baseInfo.getAbbreviationsNumber() > 0) {
			String abbrevNumber = getString(StringKey.NUMBER_OF_ABBREVIATIONS, lang) + ": <dvl>" + baseInfo.getAbbreviationsNumber() + "</dvl>";
			html += "<tr><td>" + abbrevNumber + "</td></tr>";
		}
		
		if (!baseInfo.isMediaBaseSeparate() && baseInfo.getMediaResourcesNumber() > 0) {
			String resNumber = getString(StringKey.NUMBER_OF_RESOURCES, lang) + ": <dvl>" + formatNumber(baseInfo.getMediaResourcesNumber()) + "</dvl>";
			html += "<tr><td>" + resNumber + "</td></tr>";
		}
		
		html += langDirections +
			artFormat +
			artFormatIWM +
			abbrFormat +
			baseVersion +
			codePage +
			createdBy +
			compDate + 
			basePartsNumber +
			"<tr><td>" + baseSize + "<br/><br/></td></tr>" +
			media + "</table>";
		
		return html;
	}
	
	private static String getShortDate(String longDate) {
		String res = null;
		if (longDate != null) {
			String[] dateParts = longDate.split("T");
			if (dateParts.length > 1) {
				res = dateParts[0];
			}
		}
		return res;
	}
	
	private static String wrapRow(String title, Object row, boolean condition) {
		String result = "";
		if (row != null && condition) {
			String strRow = row.toString();
			if (strRow.trim().length() > 0) {
				result= "<tr><td>" + title + ": <dvl>" + strRow + "</dvl></td></tr>";
			}
			
		}
		return result;
	}
	
	private static String formatSize(long size) {
		if (size > 1073741824) {
			return (FORMATTER_SIZE).format((double) size / 1073741824D) + " GB (" +size + " B)";
		} else {
			return (FORMATTER_SIZE).format((double) size / 1048576D) + " MB (" + size + " B)";
		}
	}
	
	private static String formatNumber(long num) {
		return (FORMATTER_NUMBER).format(num);
	}
	
	private static String formatCPString(String cp) {
		if (cp == null || cp.length() < 1) {
			return cp;
		}
		
		if (cp.toLowerCase().startsWith("utf")) {
			return cp.substring(0,3).toUpperCase() + cp.substring(3);
		} else {
	        return cp.substring(0,1).toUpperCase() + cp.substring(1).toLowerCase();
		}
	}

}
