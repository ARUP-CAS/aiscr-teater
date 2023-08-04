import { SearchType } from '../generated/globalTypes';

export const getSearchTypeToString = (type: SearchType) => {
	switch (type) {
		case SearchType.METADATA:
			return 'metadata';
		case SearchType.NAME:
			return 'category';
		default:
			return 'unknown';
	}
};

export const searchTypeTranslations = {
	category: {
		cs: 'Kategorie',
		en: 'Category',
		de: 'Kategorie',
	},
	metadata: {
		cs: 'Metadata',
		en: 'Metadata',
		de: 'Metadaten',
	},
	unknown: {
		cs: 'Neznáme',
		en: 'Unknown',
		de: 'Unbekannt',
	},
};
