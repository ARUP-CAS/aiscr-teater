import { SearchType } from '../generated/globalTypes'

export function getSearchTypeToString(type: SearchType): string {
  switch (type) {
    case SearchType.METADATA:
      return 'metadata'
    case SearchType.NAME:
      return 'category'
    default:
      return 'uknown'
  }
}

export function getTranslatedSearchType(type: SearchType): string {
  const typeString = getSearchTypeToString(type)

  switch (typeString) {
    case 'category':
      return 'Kategorie'
    case 'metadata':
      return 'Metadata'
    default:
      return 'Neznáme'
  }
}
