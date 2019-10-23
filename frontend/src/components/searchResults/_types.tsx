import { ColumnItem } from 'components/column/_types'

export interface SearchResultsColumn {
  title: string
  data: ColumnItem[]
  key: string
}

export interface SearchResultsProps {
  columns: SearchResultsColumn[]
  loading?: boolean
}
