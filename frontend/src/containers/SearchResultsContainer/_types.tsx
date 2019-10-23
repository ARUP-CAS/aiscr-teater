// eslint-disable-next-line @typescript-eslint/camelcase
import { GetSearchPage_search } from 'generated/GetSearchPage'

// eslint-disable-next-line @typescript-eslint/camelcase
export type SearchResult = GetSearchPage_search

export interface SearchResultsContainerProps {
  data: SearchResult[]
  loading: boolean
}
