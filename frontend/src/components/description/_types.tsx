import { ColumnItem } from 'components/column/_types'

export interface DescriptionData {
  columns?: { data: ColumnItem[]; key: string }[]
  title?: string
  description?: string
  loading?: boolean
}
