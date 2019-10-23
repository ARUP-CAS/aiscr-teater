import { SearchResult } from './_types'
import { SearchResultsColumn } from '../../components/searchResults/_types'
import {
  getSearchTypeToString,
  getTranslatedSearchType
} from '../../utils/utilizeSearchTypes'
import { SearchType } from '../../generated/globalTypes'

export function convertSearchResultsToColumns(
  data: SearchResult[]
): SearchResultsColumn[] {
  const initData = [
    {
      data: [],
      title: 'Výsledky v názvech hesel',
      key: getSearchTypeToString(SearchType.NAME)
    },
    {
      data: [],
      title: 'Výsledky v metadatech',
      key: getSearchTypeToString(SearchType.METADATA)
    }
  ]

  return data.reduce<SearchResultsColumn[]>((columns, current) => {
    const searchType = getSearchTypeToString(current.searchType)
    const keyInColumns = columns.find(value => value.key === searchType)

    if (keyInColumns) {
      keyInColumns.data.push(current)
    } else {
      columns.push({
        data: [current],
        title: getTranslatedSearchType(current.searchType),
        key: searchType
      })
    }

    return columns
  }, initData)
}
