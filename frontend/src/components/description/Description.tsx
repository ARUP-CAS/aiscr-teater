import React, { FC, forwardRef, useEffect, useRef } from 'react'
import times from 'lodash/times'
import { DescriptionData } from './_types'
import Column from 'components/column'
import { convertLocalLinksToNewTab } from 'utils/convertLocalLinksToNewTab'
import { Markdown } from 'components/Markdown'

const DescriptionLoading: FC = () => (
  <div className="description-content">
    <div className="loading-background-meta" />
    {times(6, index => (
      <div key={index} className="loading-background" />
    ))}
  </div>
)

const EmptyDescription: FC = () => <div className="empty" />

// eslint-disable-next-line react/display-name
const DescriptionContent = forwardRef<
  HTMLHeadingElement,
  { title: string; description: string }
>(({ title, description }, ref) => {
  const contentRef = useRef<HTMLDivElement>(null)

  useEffect(
    () => {
      convertLocalLinksToNewTab(contentRef.current)
    },
    [contentRef]
  )

  return (
    <div className="description-content">
      <h2 ref={ref}>{title}</h2>
      {description ? (
        <Markdown
          dangerouslySetInnerHTML={{ __html: description }}
          ref={contentRef}
        />
      ) : (
        <EmptyDescription />
      )}
    </div>
  )
})

// eslint-disable-next-line react/display-name
export const Description = forwardRef<HTMLHeadingElement, DescriptionData>(
  ({ columns = [], title = '', description = '', loading = false }, ref) => (
    <div className="description">
      {columns.map(column =>
        column.data.length ? (
          <Column key={column.key} data={column.data} />
        ) : null
      )}
      {loading ? (
        <DescriptionLoading />
      ) : (
        <DescriptionContent title={title} description={description} ref={ref} />
      )}
    </div>
  )
)
